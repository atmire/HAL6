package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import com.atmire.dspace.hal.xmlgenerating.elements.multi.MultiValueMetadataFieldGenerator;
import org.apache.commons.lang3.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Created by jonas on 08/05/15.
 */
public class MultiValueMappedAttributeMetadataFieldGenerator extends MultiValueMetadataFieldGenerator {


    private Map<String, String> map;

    private String attributeName;

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        String value = dcValue.getValue();
        if(StringUtils.isBlank(value)){
            return;
        }
        if (StringUtils.isNotBlank(map.get(value))) {
            String name = (StringUtils.isNotBlank(attributeName)) ? attributeName : "n";
            element.setAttribute(name, map.get(value));
        } else if (StringUtils.isNotBlank(map.get("default"))) {
            String name = (StringUtils.isNotBlank(attributeName)) ? attributeName : "n";
            element.setAttribute(name, map.get("default"));
        }

    }

    @Required
    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
