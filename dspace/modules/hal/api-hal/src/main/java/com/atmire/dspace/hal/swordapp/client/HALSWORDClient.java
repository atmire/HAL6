package com.atmire.dspace.hal.swordapp.client;

import com.atmire.dspace.hal.service.HALOnBehalfOfRetrievalService;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.swordapp.client.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jonas on 13/10/15.
 */
public class HALSWORDClient extends SWORDClient {

    private static final String HAL_HEADER_ARXIV = "Export-To-Arxiv";
    private static final String HAL_HEADER_PMC = "Export-To-PMC";
    private static final String HAL_HEADER_REPEC = "Hide-For-RePEc";
    private static final String HAL_HEADER_OAI = "Hide-In-OAI";

    private boolean halExportArxiv;
    private boolean halExportPMC;
    private boolean halHideForRepec;
    private boolean halHideInOAI;

    @Autowired
    private ItemService itemService;

    @Autowired
    private HALOnBehalfOfRetrievalService halOnBehalfOfRetrievalService;
    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(HALSWORDClient.class);

    public DepositReceipt deposit(Context context, Item item, String collectionURL, Deposit deposit, AuthCredentials auth, DepositType depositType)
            throws SWORDClientException, SWORDError, ProtocolViolationException
    {
        // some initial error checking and validation
        if (collectionURL == null)
        {
            log.error("Null URL passed into deposit method");
            throw new SWORDClientException("Null URL passed into deposit method");
        }
        if (deposit == null)
        {
            log.error("Null Deposit Object passed into deposit method");
            throw new SWORDClientException("Null Deposit Object passed into deposit method");
        }
        if (log.isDebugEnabled())
        {
            log.debug("beginning deposit on Collection url " + collectionURL);
        }

        AbderaClient client = new AbderaClient(this.abdera);
        RequestOptions options = this.getDefaultRequestOptions();
        Map<String, Boolean> additionalHeaders = getAdditionalHeaders(item);
        for(String key : additionalHeaders.keySet()){
            options.setHeader(key, additionalHeaders.get(key).toString());
        }
        String onBehalfOfHeader = halOnBehalfOfRetrievalService.retrieveOnBehalfOfHeader(context, item);
        if(StringUtils.isNotBlank(onBehalfOfHeader)){
            options.setHeader("On-Behalf-Of", onBehalfOfHeader);
        }

        HttpHeaders http = new HttpHeaders();

        // ensure that the URL is valid
        URL url = this.formaliseURL(collectionURL);
        if (log.isDebugEnabled())
        {
            log.debug("Formalised Collection URL to " + url.toString());
        }

        // sort out the HTTP basic authentication credentials
        this.prepAuth(auth, client, options);

        // log the request
        this.logDepositRequest(auth, url.toString(), deposit, "Create");

        // prepare the common HTTP headers (other than the auth ones)
        http.addInProgress(options, deposit.isInProgress());
        http.addSlug(options, deposit.getSlug());

        ClientResponse resp = null;
        if (deposit.isEntryOnly())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Connecting to server to do Entry-Only deposit at url " + url.toString());
            }
            if ((depositType == DepositType.NEW)) {
                resp = client.post(url.toString(), deposit.getEntryPart().getEntry(), options);
            } else if(StringUtils.equalsIgnoreCase(depositType.method,DepositType.Constants.PUT)){
                resp = client.put(url.toString(), deposit.getEntryPart().getEntry(), options);

            }
            if (log.isDebugEnabled())
            {
                log.debug("Successfully completed Entry-Only deposit request (doesn't mean the deposit was successful!) on url: " + url.toString());
            }
        }
        else if (deposit.isMultipart())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Connecting to server to do Multipart deposit at url " + url.toString());
            }
            resp = client.execute(depositType.getMethod(), url.toString(), new SWORDMultipartRequestEntity(deposit), options);
            // resp = client.post(url.toString(), deposit.getEntryPart().getEntry(), deposit.getFile(), deposit.getMimeType(), options);
            if (log.isDebugEnabled())
            {
                log.debug("Successfully completed Multipart deposit request (doesn't mean the deposit was successful!) on url: " + url.toString());
            }
        }
        else if (deposit.isBinaryOnly())
        {
            // add the headers specific to a binary only deposit
            http.addContentDisposition(options, deposit.getFilename());
            http.addContentMd5(options, deposit.getMd5());
            http.addPackaging(options, deposit.getPackaging());

            // prepare the content to be delivered
            InputStreamRequestEntity media = new InputStreamRequestEntity(deposit.getFile(), deposit.getMimeType());

            // carry out the deposit
            if (log.isDebugEnabled())
            {
                log.debug("Connecting to server to do Binary-Only deposit at url " + url.toString());
            }
            if ((depositType == DepositType.NEW)) {
                resp = client.post(url.toString(), media, options);
            } else if(StringUtils.equalsIgnoreCase(depositType.method, DepositType.Constants.PUT)){
                resp = client.put(url.toString(), media, options);
            }
            if (log.isDebugEnabled())
            {
                log.debug("Successfully completed Binary-Only deposit request (doesn't mean the deposit was successful!) on url: " + url.toString());
            }
        }
        else
        {
            log.error("Deposit Object does not have one/both of entry/content set; throwing Exception");
            throw new SWORDClientException("Deposit Object does not have one/both of entry/content set");
        }

        if (resp != null) {
                        int status = resp.getStatus();
            ResponseCodeManager rcm = new ResponseCodeManager();
            ResponseStatus rs = rcm.depositNew(status);
            if (rs.isCorrect() || rs.isIncorrectButAllowed()) {
                log.info("Deposit request on " + url.toString() + " returned HTTP status " + status + "; SUCCESS");

                if (rs.isIncorrectButAllowed()) {
                    log.warn("Server responded with status " + status + " which is incorrect.  Attempting to continue " +
                            "processing response ...");
                }

                // SUCCESS
                DepositReceipt receipt = this.getDepositReceipt(resp, auth);
                return receipt;
            } else if (rs.isIncorrectAndViolation()) {
                throw new ProtocolViolationException("Server responded with invalid status " + status);
            } else if (rs.isError()) {
                // FIXME: this needs to handle all the other possible response codes
                log.info("Deposit request on " + url.toString() + " returned Error HTTP status " + status);
                ErrorHandler eh = new ErrorHandler();
                throw eh.handleError(resp);
            } else {
                throw new ProtocolViolationException("Unexpected response code " + status);
            }
        }
        return null;
    }

    private Map<String, Boolean> getAdditionalHeaders(Item item) {
        Map<String, Boolean> additionalHeaders = new HashMap<>();
        boolean halExportArxiv = getAdditionalHeadersFromMetadataIfPresent(item, "hal.export.arxiv", this.halExportArxiv);
        boolean halExportPMC = getAdditionalHeadersFromMetadataIfPresent(item, "hal.export.pmc", this.halExportPMC);
        boolean halHideForRepec = getAdditionalHeadersFromMetadataIfPresent(item, "hal.hide.repec", this.halHideForRepec);
        boolean halHideInOAI = getAdditionalHeadersFromMetadataIfPresent(item, "hal.hide.oai", this.halHideInOAI);

        additionalHeaders.put(HAL_HEADER_ARXIV, halExportArxiv);
        additionalHeaders.put(HAL_HEADER_PMC, halExportPMC);
        additionalHeaders.put(HAL_HEADER_REPEC, halHideForRepec);
        additionalHeaders.put(HAL_HEADER_OAI, halHideInOAI);

        return additionalHeaders;
    }

    private boolean getAdditionalHeadersFromMetadataIfPresent(Item item, String metadata, boolean defaultValue){
        List<MetadataValue> itemToDepositMetadata = itemService.getMetadataByMetadataString(item,metadata);
        if(itemToDepositMetadata.size()>0){
            String value = itemToDepositMetadata.get(0).getValue();
            if(StringUtils.isNotBlank(value)){
                if(StringUtils.equalsIgnoreCase(value,"oui") || StringUtils.equals(value, "1")){
                    return true;
                } if( StringUtils.equalsIgnoreCase(value,"non") || StringUtils.equals(value, "0")){
                    return false;
                }
                return Boolean.valueOf(value);
            }
        }
        return defaultValue;
    }

    @Required
    public void setHalExportArxiv(boolean halExportArxiv) {
        this.halExportArxiv = halExportArxiv;
    }
    @Required
    public void setHalExportPMC(boolean halExportPMC) {
        this.halExportPMC = halExportPMC;
    }

    @Required
    public void setHalHideForRepec(boolean halHideForRepec) {
        this.halHideForRepec = halHideForRepec;
    }

    @Required
    public void setHalHideInOAI(boolean halHideInOAI) {
        this.halHideInOAI = halHideInOAI;
    }

    public enum DepositType {
        NEW (Constants.POST,"newSubmission"),
        UPDATE (Constants.PUT,"newVersion"),
        UPDATE_METADATA (Constants.PUT,"metadataUpdate");

        private String method;
        private String typeString;

        DepositType(String method, String typeString) {
            this.method = method;
            this.typeString = typeString;
        }
        public String getMethod() {
            return method;
        }

        public String getTypeString() {
            return typeString;
        }

        private static class Constants {
            static final String POST = "POST";
            static final String PUT = "PUT";
        }
    }
}
