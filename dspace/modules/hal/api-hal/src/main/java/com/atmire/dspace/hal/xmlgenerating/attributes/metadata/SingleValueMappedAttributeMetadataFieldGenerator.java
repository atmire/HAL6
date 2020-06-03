package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by jonas - jonas@atmire.com on 13/02/2018.
 */
public class SingleValueMappedAttributeMetadataFieldGenerator extends MetadataValueAsAttributeGenerator{

    Map<String, String> map;

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        String value = dcValue.getValue();
        if(StringUtils.isNotBlank(value)&& map.containsKey(value)){
            element.setAttribute(attributeName, map.get(value));
        }
    }

    @Required
    public void setMap(Map<String, String> mappedValuePairs) {
        this.map = mappedValuePairs;
    }
}
