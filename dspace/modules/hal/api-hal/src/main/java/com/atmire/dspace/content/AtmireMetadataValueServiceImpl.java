package com.atmire.dspace.content;

import com.atmire.dspace.content.service.AtmireMetadataValueService;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.content.MetadataValueServiceImpl;
import com.atmire.dspace.content.service.AtmireItemService;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by jonas - jonas@atmire.com on 29/09/17.
 */
public class AtmireMetadataValueServiceImpl extends MetadataValueServiceImpl implements AtmireMetadataValueService {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(AtmireMetadataValueServiceImpl.class);

    @Autowired
    AuthorizeService authorizeService;
    @Autowired
    AtmireItemService atmireItemService;
    @Override
    public void addChildMetadataValue(Context context, Item item, MetadataValue parentMetadataValue, MetadataValue childMetadataValue) throws AuthorizeException, SQLException {
        authorizeService.authorizeAction(context,item, Constants.WRITE);
        parentMetadataValue.addMetadataValue(childMetadataValue);
        childMetadataValue.setParentMetadataValue(parentMetadataValue);
    }

    @Override
    public void removeRelations(Context context, Item item, MetadataValue parentMetadataValue) throws AuthorizeException, SQLException {
        authorizeService.authorizeAction(context,item, Constants.WRITE);
        final Iterator<MetadataValue> childMetadataValues = parentMetadataValue.getChildMetadataValues().iterator();
        while (childMetadataValues.hasNext())
        {
            final MetadataValue childValue = childMetadataValues.next();
            childMetadataValues.remove();
            childValue.setParentMetadataValue(null);
            parentMetadataValue.removeMetadataValue(childValue);
            atmireItemService.removeMetadataValue(context, item,childValue);
            delete(context,childValue);
        }
    }


}
