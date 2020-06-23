package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.prechecked.ConditionCheckingDataAttributGenerator;
import com.atmire.dspace.hal.xmlgenerating.config.MetadataCondition;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 27/04/15.
 */
public class MultiValueMetadataFieldGenerator implements HalSwordDataElementGenerator {


    private String metadataField;
    private String elementName;
    protected String defaultValue;
    protected List<MetadataCondition> metadataConditions = new ArrayList<MetadataCondition>();


    protected List<HALSwordDataAttributeGenerator> attributes = new ArrayList<HALSwordDataAttributeGenerator>();

    public Element generateElement(Document doc, Element parent, Item item) {
        List<MetadataValue> dcValues = new ArrayList<>();
        if (StringUtils.isNotBlank(metadataField)) {
            dcValues = item.getItemService().getMetadataByMetadataString(item, metadataField);
        }

        if (CollectionUtils.isEmpty(dcValues) && StringUtils.isNotBlank(defaultValue)) {
            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addDefaultValue(element);
            addAllAttributes(item,element);
        }

        for (MetadataValue dcv : dcValues) {
            if (!checkIfConditionsMet(dcv))return parent;

            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addAllAttributes(item,dcv, element);
            attachMetadataValue(doc, element, item, dcv);
        }
        return parent;
    }

    public void addDefaultValue(Element element) {
        if(StringUtils.isNotBlank(defaultValue)){
            element.setNodeValue(defaultValue);
        }
    }
    protected boolean checkIfConditionsMet(MetadataValue dcv) {
        for(MetadataCondition condition : metadataConditions){
            if(!condition.matchesCondiction(dcv)){
                return false;
            }
        }
        return true;
    }

    private void addAllAttributes(Item item, MetadataValue dcv, Element element) {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            if(atts instanceof ConditionCheckingDataAttributGenerator){
                ((ConditionCheckingDataAttributGenerator) atts).addAttribute(item, element);
            }else{
                atts.addAttribute(dcv, element);
            }
        }
    }

    private void addAllAttributes(Item item, Element element) {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            if(atts instanceof ConditionCheckingDataAttributGenerator){
                ((ConditionCheckingDataAttributGenerator) atts).addAttribute(item, element);
            }else{
                atts.addAttribute(null,element);
            }
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

    public void setMetadataConditions(List<MetadataCondition> metadataConditions) {
        this.metadataConditions = metadataConditions;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
