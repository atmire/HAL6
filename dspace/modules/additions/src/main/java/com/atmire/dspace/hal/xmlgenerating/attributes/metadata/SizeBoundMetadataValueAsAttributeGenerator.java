package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by jonas - jonas@atmire.com on 14/03/2018.
 */
public class SizeBoundMetadataValueAsAttributeGenerator extends MetadataValueAsAttributeGenerator {

    private int currentCount = 0;
    private int sizeConstraint = -1;

    public Element generateElement(Document doc, Element parent, Item item) {
        reset();
        return super.generateElement(doc, parent, item);
    }

    private void reset() {
        this.currentCount = 0;
    }

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        if (sizeConditionExceeded()) {
            return;
        }
        String value = getValue(dcValue);
        if (StringUtils.isNotBlank(value)) {
            element.setAttribute(attributeName, value);
            currentCount++;
        }
    }

    protected String getValue(MetadataValue dcValue) {
        return dcValue.getValue();
    }

    protected boolean checkIfConditionsMet(MetadataValue dcv) {
        if(sizeConditionExceeded()){
            return false;
        }
        return super.checkIfConditionsMet(dcv);
    }

    private boolean sizeConditionExceeded() {
        return sizeConstraint != -1 && currentCount >= sizeConstraint;
    }

    public void setSizeConstraint(int sizeConstraint){
        this.sizeConstraint = sizeConstraint;
    }
}
