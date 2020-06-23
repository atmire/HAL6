package com.atmire.dspace.hal.xmlgenerating.elements.single;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.config.MetadataExistsCondition;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.apache.commons.lang3.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 27/04/15.
 */
public class SingleValueMetadataFieldGenerator implements HalSwordDataElementGenerator {


    protected String metadataField;
    protected String elementName;
    protected String defaultValue;
    protected MetadataExistsCondition condition;
    protected List<HALSwordDataAttributeGenerator> attributes = new ArrayList<HALSwordDataAttributeGenerator>();

    public Element generateElement(Document doc, Element parent, Item item) {
        if(condition!=null){
            if(!condition.exists(item)){
                return parent;
            }
        }
        List<MetadataValue> dcValues = null;
        if (StringUtils.isNotBlank(metadataField)) {
            dcValues = item.getItemService().getMetadataByMetadataString(item, metadataField);
        }

        if(dcValues!=null && dcValues.size()>0 && StringUtils.isNotBlank(dcValues.get(0).getValue())){
            MetadataValue dcv = dcValues.get(0);
            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addAllAttributes(dcv, element);
            attachMetadataValue(doc, element, item, dcv);
        } else if(StringUtils.isNotBlank(defaultValue)){
            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addAllAttributes(null, element);
            element.appendChild(doc.createTextNode(defaultValue));
        }
        return parent;
    }


    protected void addAllAttributes(MetadataValue dcv, Element element) {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            atts.addAttribute(dcv, element);
        }
    }

    //append a textnode based on the value to the element
    public void attachMetadataValue(Document doc, Element element, Item item, MetadataValue dcValue) {
        element.appendChild(doc.createTextNode(dcValue.getValue()));

    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getMetadataField() {
        return metadataField;
    }

    public void setMetadataField(String metadataField) {
        this.metadataField = metadataField;
    }

    public void setAttributes(List<HALSwordDataAttributeGenerator> attributes) {
        this.attributes = attributes;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setCondition(MetadataExistsCondition condition) {
        this.condition = condition;
    }

}


