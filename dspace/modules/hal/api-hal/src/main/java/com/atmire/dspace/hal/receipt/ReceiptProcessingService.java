package com.atmire.dspace.hal.receipt;

import com.atmire.dspace.hal.swordapp.client.HALSWORDClient;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.swordapp.client.DepositReceipt;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public interface ReceiptProcessingService {

    void processReceipt(Context context, DepositReceipt item, Item receipt, HALSWORDClient.DepositType depositType) throws SQLException, ParserConfigurationException, IOException, SAXException;
}
