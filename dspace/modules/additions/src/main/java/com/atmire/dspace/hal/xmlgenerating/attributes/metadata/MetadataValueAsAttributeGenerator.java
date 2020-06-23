package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import com.atmire.dspace.hal.xmlgenerating.elements.multi.MultiValueMetadataFieldGenerator;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by jonas on 08/05/15.
 */
// Class intended to add the value of a given metadata-element as an attribute to an element
public class MetadataValueAsAttributeGenerator extends MultiValueMetadataFieldGenerator {

    protected String attributeName;

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        if(StringUtils.isNotBlank(dcValue.getValue())) element.setAttribute(attributeName, dcValue.getValue());

    }

    public void addDefaultValue(Element element) {
        if(StringUtils.isNotBlank(defaultValue)){
            element.setAttribute(attributeName, defaultValue);
        }
    }

    @Required
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}

