package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/15
 * Time: 09:41
 */
public class MultiValueMappedMetadataFieldGenerator extends MultiValueMetadataFieldGenerator
{
    protected Map<String, String> map;

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        String value = dcValue.getValue();
        if(org.apache.commons.lang3.StringUtils.isBlank(value)){
            return;
        }
        if (StringUtils.isNotBlank(map.get(value))) {
            element.appendChild(doc.createTextNode(map.get(value)));
        }else{
            super.attachMetadataValue(doc, element, i ,dcValue);
        }
    }

    @Required
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
