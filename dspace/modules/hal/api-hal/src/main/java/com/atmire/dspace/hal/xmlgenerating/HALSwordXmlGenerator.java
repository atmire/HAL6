package com.atmire.dspace.hal.xmlgenerating;

import org.dspace.content.Item;
import org.dspace.core.Context;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jonas on 24/04/15.
 */
public interface HALSwordXmlGenerator {

    public File generateXMLFromItem(Context context, Item item) throws SQLException, IOException;


}
