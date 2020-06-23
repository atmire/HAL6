package com.atmire.dspace.content.service;

/**
 * Created by jonas - jonas@atmire.com on 23/10/17.
 */

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Service interface class for the Item object.
 * The implementation of this class is responsible for all business logic calls for the Item object and is autowired by spring
 *
 * @author kevinvandevelde at atmire.com
 */
public interface AtmireItemService extends ItemService {

    public void removeMetadataValue(Context context, Item dso, MetadataValue value) throws SQLException, AuthorizeException;

    void removeSingleMetadataValue(Context context, Item dso, MetadataValue values, boolean ignoreMetadataRelations) throws SQLException, AuthorizeException;
}
