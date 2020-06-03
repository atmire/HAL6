package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.HalSwordStringDataAttributeGenerator;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Element;

/**
 * Created by jonas on 08/05/15.
 */
public class ValueAsAttributeGenerator implements HALSwordDataAttributeGenerator, HalSwordStringDataAttributeGenerator {

    protected String name;
    protected String value;

    @Override
    public void addAttribute(MetadataValue dcValue, Element element) {
        if (dcValue == null) {
            addAttribute(element);
        } else {
            element.setAttribute(name, value);
        }
    }

    @Override
    public void addAttribute(Element element) {
        element.setAttribute(name, value);
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }

    @Required
    public void setValue(String value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
