package com.atmire.dspace.hal.xmlgenerating.config;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by jonas - jonas@atmire.com on 09/02/2018.
 */
public class MetadataValueEqualsCondition {

    String metadataField;
    String valueToMatch;

    public boolean conditionMet(Item item){
        return StringUtils.equals(item.getItemService().getMetadata(item, metadataField), valueToMatch);
    }

    @Required
    public void setMetadataField(String metadataField) {
        this.metadataField = metadataField;
    }
    @Required
    public void setValueToMatch(String valueToMatch) {
        this.valueToMatch = valueToMatch;
    }

}
