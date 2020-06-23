package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.prechecked.ConditionCheckingDataAttributGenerator;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by jonas - jonas@atmire.com on 19/03/2018.
 */
public class SplitMultiValueMetadataFieldGenerator extends MultiValueMetadataFieldGenerator {

    /* Log4j logger*/
    private static final Logger log = Logger.getLogger(SplitMultiValueMetadataFieldGenerator.class);

    private String separator;
    private int index;

    protected void addAllAttributes(Item item, MetadataValue dcv, Element element) {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            if (atts instanceof ConditionCheckingDataAttributGenerator) {
                ((ConditionCheckingDataAttributGenerator) atts).addAttribute(item, element);
            } else {
                atts.addAttribute(dcv, element);
            }
        }
    }

    //append a textnode based on the value to the element
    public void attachMetadataValue(Document doc, Element element, Item item, MetadataValue dcValue) {
        String value = dcValue.getValue();
        if (value.contains(separator)) {
            String[] splitValues = value.split(separator);
            if (splitValues.length > index) {
                value = splitValues[index];
            } else {
                log.warn("Invalid index provided for value \"" + value + "\" split on separator " + separator+". Skipping creating this element.");
                return;
            }
        } else {
            log.warn("The provided separator \"" + separator + "\" can not be found on value: " + value+". Skipping creating this element.");
            return;
        }
        element.appendChild(doc.createTextNode(value));
    }

    @Required
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Required
    public void setIndex(int index) {
        this.index = index;
    }
}
