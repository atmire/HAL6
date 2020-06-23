package com.atmire.dspace.hal.xmlgenerating.elements.single;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Created by jonas - jonas@atmire.com on 13/02/2018.
 */
public class SingleLineMappedMetadataFieldGenerator extends SingleLineMultiAttributeMetadataFieldGenerator {

    Map<String, String> map;

    protected String getValue(Item item) {
        String value = super.getValue(item);
        if (StringUtils.isNotBlank(value) && map.containsKey(value)) {
            return map.get(value);
        }
        return value;
    }

    @Required
    public void setMap(Map<String, String> mappedValuePairs) {
        this.map = mappedValuePairs;
    }
}
