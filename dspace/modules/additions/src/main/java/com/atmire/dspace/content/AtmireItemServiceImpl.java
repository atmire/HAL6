package com.atmire.dspace.content;

import com.atmire.dspace.content.service.AtmireItemService;
import org.apache.commons.lang.StringUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.content.ItemServiceImpl;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by jonas - jonas@atmire.com on 23/10/17.
 */
public class AtmireItemServiceImpl  extends ItemServiceImpl implements AtmireItemService {

    @Override
    public void removeMetadataValue(Context context, Item dso, MetadataValue value) throws SQLException, AuthorizeException, AuthorizeException {
        Iterator<MetadataValue> metadata = getMetadata(dso, Item.ANY, Item.ANY, Item.ANY, Item.ANY).iterator();
        while (metadata.hasNext()) {
            MetadataValue iteratorValue = metadata.next();

            if (StringUtils.equals(value.getValue(), iteratorValue.getValue()) && iteratorValue.getMetadataField().equals(value.getMetadataField())) {
                metadata.remove();
                metadataValueService.delete(context, value);
            }
        }
    }

    public void removeSingleMetadataValue(Context context, Item dso, MetadataValue value, boolean ignoreMetadataRelations) throws SQLException, AuthorizeException {
        if (!ignoreMetadataRelations) {
            removeMetadataValues(context, dso, Arrays.asList(value));
        } else {
            Iterator<MetadataValue> metadata = getMetadata(dso, Item.ANY, Item.ANY, Item.ANY, Item.ANY).iterator();
            while (metadata.hasNext()) {
                MetadataValue metadataValue = metadata.next();
                if (ignoreMetadataRelations) {
                    if (metadataValue.equalsIgnoreRelations(value)) {
                        metadata.remove();
                        metadataValueService.delete(context, metadataValue);

                    }
                }
            }
        }
    }
}
