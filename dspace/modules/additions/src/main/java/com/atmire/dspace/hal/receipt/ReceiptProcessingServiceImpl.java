package com.atmire.dspace.hal.receipt;

import com.atmire.dspace.hal.service.HALStatusService;
import com.atmire.dspace.hal.swordapp.client.HALSWORDClient;
import org.apache.abdera.model.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.content.Bundle;
import org.dspace.content.DCDate;
import org.dspace.content.Item;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.swordapp.client.DepositReceipt;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public class ReceiptProcessingServiceImpl implements ReceiptProcessingService {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(ReceiptProcessingServiceImpl.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private HALStatusService statusService;


    @Override
    public void processReceipt(Context context, DepositReceipt receipt, Item item, HALSWORDClient.DepositType depositType) throws SQLException, ParserConfigurationException, IOException, SAXException {
        String dateString = addTransferredDate(context, item);
        String receiptString = receipt.getEntry().toString();
        Document doc = createDocFromReceiptString(receiptString);
        Entry entry = receipt.getEntry();

        String provenanceString ="Deposit ("+depositType.getTypeString()+") to HAL done by: "+context.getCurrentUser().getEmail() + " on "+dateString+".";
        fillInMetadata(context, item, doc, entry, provenanceString);
    }

    private void fillInMetadata(Context context, Item item, Document doc, Entry entry, String provenanceString) throws SQLException {
        String halID = entry.getId().toString();
        String password = getHalPassword(doc);
        String version = getHalVersion(doc);
        itemService.setMetadataSingleValue(context, item, "hal", "identifier", null, null, halID);
        itemService.setMetadataSingleValue(context, item, "hal", "password", null, null, password);
        itemService.setMetadataSingleValue(context, item, "hal", "version", null, null, version);

        int originalBitstreams = 0;
        for (Bundle bundle : itemService.getBundles(item, "ORIGINAL")) {
            originalBitstreams += bundle.getBitstreams().size();
        }
        provenanceString += "No of bitstreams: " + originalBitstreams + ".";
        String retrieveHALStatus = statusService.retrieveHALStatus(halID, version);
        provenanceString += "Status for " + halID + "v" + version + ": " + retrieveHALStatus;
        itemService.addMetadata(context, item, "hal", "description", "provenance", null, provenanceString);
    }

    private String addTransferredDate(Context context, Item item) throws SQLException {
        Date date = new Date();
        DCDate today = new DCDate(date);
        String value = today.toString();
        itemService.setMetadataSingleValue(context, item, "hal", "date", "transferred", null, value);
        return value;
    }

    private Document createDocFromReceiptString(String receiptString) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(receiptString));
        return builder.parse(src);
    }


    private String getHalPassword(Document doc) {
        NodeList passwords = doc.getElementsByTagName("hal:password");
        for (int i = 0; i < passwords.getLength(); i++) {
            if (StringUtils.isNotBlank(passwords.item(i).getTextContent())) {
                return passwords.item(i).getTextContent();
            }
        }
        return null;
    }

    private String getHalVersion(Document doc) {
        NodeList versions = doc.getElementsByTagName("hal:version");
        for (int i = 0; i < versions.getLength(); i++) {
            if (StringUtils.isNotBlank(versions.item(i).getTextContent())) {
                return versions.item(i).getTextContent();
            }
        }
        return null;
    }
}
