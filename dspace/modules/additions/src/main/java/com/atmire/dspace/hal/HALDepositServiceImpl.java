package com.atmire.dspace.hal;

import com.atmire.dspace.hal.receipt.ReceiptProcessingService;
import com.atmire.dspace.hal.service.HALDepositService;
import com.atmire.dspace.hal.service.HALStatusService;
import com.atmire.dspace.hal.service.HALValidityCheckerService;
import com.atmire.dspace.hal.swordapp.client.HALSWORDClient;
import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGenerator;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.content.service.BitstreamService;
import org.dspace.content.service.BundleService;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.swordapp.client.AuthCredentials;
import org.swordapp.client.Deposit;
import org.swordapp.client.DepositReceipt;
import org.swordapp.client.SWORDError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 07/09/17.
 */
public class HALDepositServiceImpl implements HALDepositService {

    /* Log4j logger*/
    private static final Logger log = Logger.getLogger(HALDepositServiceImpl.class);
    private static final String ORIGINAL = "ORIGINAL";
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    BundleService bundleService;
    @Autowired
    BitstreamService bitstreamService;
    @Autowired
    ItemService itemService;
    @Autowired
    HALSwordXmlGenerator halSwordXmlGenerator;
    @Autowired
    HALValidityCheckerService halValidityCheckerService;
    @Autowired
    ReceiptProcessingService receiptProcessor;
    @Autowired
    HALSWORDClient swordClient;
    @Autowired
    HALStatusService halStatusService;
    @Autowired
    AuthorizeService authorizeService;

    private String serviceDocumentUrl;
    private String user;
    private String pass;
    private boolean keepHALExportAsBitstream = true;

    public void depositSubmissionToHal(Context context, Item item, boolean metadataOnly) throws SQLException, AuthorizeException, IOException {
        if (item == null) {
            return;
        }
        if(!isValidHalItem(context, item)){
            log.info(LogManager.getHeader(context,"hal_deposit_stopped" ,"The following item was found to be invalid to be sent, deposit stopped preemptively: " + item.getHandle()));
            return;
        }
        //  Check if we have a HAL identifier & version number, if so use a new update method & else use the “depositNewSubmissionToHal”.
        String version = itemService.getMetadata(item,"hal.version");
        String identifier = itemService.getMetadata(item,"hal.identifier");
        itemService.clearMetadata(context, item,"hal","update","status", Item.ANY);
        if(StringUtils.isNotBlank( version) && StringUtils.isNotBlank(identifier)) {
            if (prerequisiteCheckFailed(context, item, version, identifier)){
                return;
            }
            if(metadataOnly){
                updateMetadataInHal(context, item, identifier, version);
            }else{
                updateSubmissionInHal(context, item, identifier, version);
            }
        } else {
            depositNewSubmissionToHal(context, item);
        }
    }

    private boolean prerequisiteCheckFailed(Context context, Item item, String version, String identifier) throws SQLException {
        log.info(LogManager.getHeader(context, "hal_deposit_update_start", "Trying to update the deposit for item: " + item.getHandle()));
        if(!StringUtils.equals(halStatusService.retrieveHALStatus(identifier, version),"accept")){
            itemService.setMetadataSingleValue(context, item, "hal", "update", "status", null, HALSWORDClient.DepositType.UPDATE.getTypeString());
            log.info(LogManager.getHeader(context,"hal_update_stopped" ,"The following item was found not to be accepted yet in hal, update stopped preemptively: " + item.getHandle()));
            return true;
        }
        return false;
    }

    protected void depositNewSubmissionToHal(Context context, Item item) throws SQLException, AuthorizeException, IOException {
        log.info(LogManager.getHeader(context, "hal_deposit_start", "Trying to create a new deposit for item: " + item.getHandle()));
        depositItemToUrl(context, item, serviceDocumentUrl, HALSWORDClient.DepositType.NEW);
    }

    protected void updateSubmissionInHal(Context context, Item item, String identifier, String version) throws SQLException, AuthorizeException, IOException {
        String serviceDocumentUrlWithIdAndVersion = configurationService.getProperty("hal.url")+identifier+"v"+version;
        depositItemToUrl(context, item, serviceDocumentUrlWithIdAndVersion, HALSWORDClient.DepositType.UPDATE);
    }

    protected void updateMetadataInHal(Context context, Item item, String identifier, String version) throws SQLException, AuthorizeException, IOException {
        String serviceDocumentUrlWithIdAndVersion = configurationService.getProperty("hal.url")+identifier+"v"+version;
        Deposit deposit = null;
        try{
            deposit = createMetadataOnlyDeposit(context, item);
            depositAndProcess(context, item, serviceDocumentUrlWithIdAndVersion, HALSWORDClient.DepositType.UPDATE_METADATA, deposit);
        } finally {
            if(deposit!=null && deposit.getFile()!= null){
                deposit.getFile().close();
            }
        }
        itemService.update(context, item);
    }

    private Deposit createMetadataOnlyDeposit(Context context, Item item) throws SQLException {

        try {
            Deposit deposit = new Deposit();
            deposit.setFile(new FileInputStream(halSwordXmlGenerator.generateXMLFromItem(context, item)));
            deposit.setMimeType("text/xml");
            deposit.setFilename("hal-sword-export-" + item.getID() + ".xml");
            deposit.setPackaging("http://purl.org/net/sword-types/AOfr");
            return deposit;
        } catch (IOException e) {
            log.error(LogManager.getHeader(context, "hal_deposit_creation", "An error occurred when creating the deposit for item " + item.getID()));
        }
        return null;
    }

    private void depositItemToUrl(Context context, Item item, String serviceDocumentUrl, HALSWORDClient.DepositType depositType) throws SQLException, AuthorizeException, IOException {
        File zipFile = null;
        Deposit deposit = null;
        try {
            zipFile = compressToZip(context, item);
            if (zipFile == null) {
                log.warn(LogManager.getHeader(context, "hal_deposit_zip_fail", "Failure while compressing item " + item.getHandle() + " to a zip, skipping the deposit altogether"));
                return;
            }

            deposit = createDeposit(item, zipFile);
            depositAndProcess(context, item, serviceDocumentUrl, depositType, deposit);
        } catch (ZipException | IOException e) {
            itemService.addMetadata(context, item, "hal", "update-error", "status", null, depositType.getTypeString());
            log.error(LogManager.getHeader(context, "hal_deposit_general_error", "item: " + item.getID()), e);
        } finally {
            itemService.update(context, item);
            if (zipFile != null && zipFile.exists()) {
                zipFile.delete();
            }
            if(deposit!=null && deposit.getFile()!= null){
                deposit.getFile().close();
            }
        }
    }

    private void depositAndProcess(Context context, Item item, String serviceDocumentUrl, HALSWORDClient.DepositType depositType, Deposit deposit) throws SQLException, AuthorizeException {
        if(deposit == null){
            log.error(LogManager.getHeader(context, "hal_empty_deposit", "A blank deposit was found while depositing item " + item.getID()));
            return;
        }
        try {
            itemService.clearMetadata(context, item, "hal", "description", "error", Item.ANY);

            DepositReceipt receipt = swordClient.deposit(context, item, serviceDocumentUrl, deposit, new AuthCredentials(user, pass), depositType);
            if (receipt != null) {
                log.info(LogManager.getHeader(context, "hal_deposit_success", "A non-empty receipt has been returned while depositing item " + item.getID()));
                receiptProcessor.processReceipt(context, receipt, item, depositType);
            } else {
                log.warn(LogManager.getHeader(context, "hal_deposit_no_receipt", "An empty receipt has been returned while depositing item " + item.getID()));
            }
        } catch (SWORDError swordError) {
            itemService.addMetadata(context, item, "hal", "description", "error", null, swordError.getErrorBody());
            itemService.addMetadata(context, item, "hal", "update-error", "status", null, depositType.getTypeString());
            itemService.update(context, item);
            log.error(LogManager.getHeader(context, "hal_deposit_sword_error", "item: " + item.getID()), swordError);
        } catch (Exception e) {
            itemService.addMetadata(context, item, "hal", "update-error", "status", null, depositType.getTypeString());
            log.error(LogManager.getHeader(context, "hal_deposit_general_error", "item: " + item.getID()), e);
        }
    }

    private Deposit createDeposit(Item item, File zipFile) throws ZipException, IOException, SQLException, AuthorizeException {
        Deposit deposit = new Deposit();
        deposit.setFile(new FileInputStream(zipFile));
        deposit.setMimeType("application/zip");
        deposit.setFilename("hal-sword-export-" + item.getID() + ".xml");
        deposit.setPackaging("http://purl.org/net/sword-types/AOfr");
        return deposit;
    }

    public File compressToZip(Context context, Item item) throws IOException, SQLException, AuthorizeException {
        // Generate the xml based on the item
        File file = halSwordXmlGenerator.generateXMLFromItem(context, item);

        if (keepHALExportAsBitstream) {
            addHalFileToItem(context, item, file);
        }
        try {
            ZipFile zipFile = new ZipFile(file.getParent() + "/archive.zip");
            ZipParameters param = new ZipParameters();
            param.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            zipFile.addFile(file, param);

            addOriginalBitStreamsFromItem(context, item, zipFile);

            return zipFile.getFile();
        } catch (ZipException e) {
            log.error(LogManager.getHeader(context, "hal_zip_compressions_error", "Error while compressing bitstreams to zip for item: " + item.getID()), e);
        } finally {
            file.delete();
        }

        return null;
    }

    private void addHalFileToItem(Context context, Item item, File file) throws SQLException, AuthorizeException, IOException {
        String fileName = "hal-sword-export-" + item.getID() + ".xml";
        List<Bundle> bundles = item.getItemService().getBundles(item, "HAL");
        Bundle bundle;
        if (CollectionUtils.isNotEmpty(bundles)) {
            bundle = bundles.get(0);
            for (Bitstream bitstream : bundle.getBitstreams()) {
                bundleService.removeBitstream(context, bundle, bitstream);
            }
        } else {
            bundle = bundleService.create(context, item, "HAL");
        }
        Bitstream bitstream = bitstreamService.create(context, bundle, FileUtils.openInputStream(file));
        bitstream.setName(context, fileName);
        bitstreamService.update(context, bitstream);
        bundleService.update(context, bundle);
    }

    private void addOriginalBitStreamsFromItem(Context context, Item item, ZipFile zipFile) throws SQLException, ZipException, IOException, AuthorizeException {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setSourceExternalStream(true);
        for (Bundle b : item.getBundles()) {
            if (b.getName().equals(ORIGINAL)) {
                for (Bitstream bitstream : b.getBitstreams()) {
                    if(halValidityCheckerService.checkEmbargoValidForExport(context, bitstream))
                    {
                        parameters.setFileNameInZip(bitstream.getName());
                        zipFile.addStream(bitstreamService.retrieve(context, bitstream), parameters);
                    }
                }
            }
        }
    }



    @Override
    public boolean isValidHalItem(Context context, Item item) throws SQLException, AuthorizeException {
        return halValidityCheckerService.isValidHalItem(context, item);
    }

    @Required
    public void setServiceDocumentUrl(String serviceDocumentUrl) {
        this.serviceDocumentUrl = serviceDocumentUrl;
    }

    @Required
    public void setUser(String user) {
        this.user = user;
    }

    @Required
    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setKeepHALExportAsBitstream(boolean keepHALExportAsBitstream) {
        this.keepHALExportAsBitstream = keepHALExportAsBitstream;
    }
}
