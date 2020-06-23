package com.atmire.dspace.hal.xmlgenerating.config;

import org.apache.commons.collections.CollectionUtils;
import org.dspace.content.Item;
import org.dspace.content.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by jonas - jonas@atmire.com on 19/12/17.
 */
public class MetadataExistsCondition {

    @Autowired
    private ItemService itemService;

    private String metadataField;

    public boolean exists(Item item){
        if(CollectionUtils.isNotEmpty(itemService.getMetadataByMetadataString(item,metadataField))){
            return true;
        }
        return false;
    }

    @Required
    public void setMetadataField(String metadataField) {
        this.metadataField = metadataField;
    }

}
