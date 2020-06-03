package com.atmire.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.content.service.MetadataValueService;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 29/09/17.
 */
public interface AtmireMetadataValueService extends MetadataValueService {

    void addChildMetadataValue(Context context, Item item, MetadataValue parentMetadataValue, MetadataValue childMetadataValue) throws AuthorizeException, SQLException;

    void removeRelations(Context context, Item item, MetadataValue parentMetadataValue) throws AuthorizeException, SQLException;

}
