package com.atmire.dspace.hal.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 07/11/17.
 */
public interface HALValidityCheckerService {

    boolean isValidHalItem(Context context, Item item) throws SQLException, AuthorizeException;

    boolean checkEmbargoValidForExport(Context context, Bitstream bitstream) throws SQLException;
}
