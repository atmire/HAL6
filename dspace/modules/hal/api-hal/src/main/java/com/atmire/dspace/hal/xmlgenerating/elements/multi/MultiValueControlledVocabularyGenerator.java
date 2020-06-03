package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by jonas - jonas@atmire.com on 29/01/2018.
 */
public class MultiValueControlledVocabularyGenerator extends MultiValueMetadataFieldGenerator{

    //append a textnode based on the value to the element
    public void attachMetadataValue(Document doc, Element element, Item item, MetadataValue dcValue) {
        String jelValue = dcValue.getValue();
        String[] split = jelValue.split("::");
        String output = "";
        for(String splitValue : split){
            if (splitValue.contains(" - ")) {
                String id = StringUtils.split(splitValue, " - ")[0];
                if (StringUtils.isNotBlank(output)) {
                    output += ".";
                }
                output += id.trim();
            }
        }
        element.appendChild(doc.createTextNode(output));
    }
}
