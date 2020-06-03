package com.atmire.dspace.hal.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 07/09/17.
 */
public interface HALDepositService {

    void depositSubmissionToHal(Context context, Item item, boolean metadataOnly) throws SQLException, AuthorizeException, IOException;

    boolean isValidHalItem(Context context, Item item) throws SQLException, AuthorizeException;

}
