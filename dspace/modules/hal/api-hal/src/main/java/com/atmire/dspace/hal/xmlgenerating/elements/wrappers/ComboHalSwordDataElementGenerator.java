package com.atmire.dspace.hal.xmlgenerating.elements.wrappers;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.bitstream.BitstreamDependentAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.metadata.ValueAsAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import com.atmire.dspace.hal.xmlgenerating.filter.DisabledElementFilter;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 24/04/15.
 */
public class ComboHalSwordDataElementGenerator implements HalSwordDataElementGenerator {

    private List<HalSwordDataElementGenerator> bodyElementGenerators;
    protected List<HALSwordDataAttributeGenerator> attributes = new ArrayList<>();
    protected List<BitstreamDependentAttributeGenerator> bitstreamAttributes = new ArrayList<>();
    protected List<DisabledElementFilter> disabledFilter =new ArrayList<DisabledElementFilter>();
    protected String elementName;



    public Element generateElement(Document doc, Element parent,Item item) throws SQLException {
        for(DisabledElementFilter filter: disabledFilter){
            // if the filtered element is not present -> do nothing
            if(filter.checkIfFilterElementIsAbsent(item)){
                return parent;
            }
        }

        Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
        parent.appendChild(element);
        addAllAttributes(element,item);
        boolean bodyAdded = false;
        if (bodyElementGenerators != null) {
            for (HalSwordDataElementGenerator dataElementGenerator : bodyElementGenerators) {
                Element bodyElement = dataElementGenerator.generateElement(doc, element,item);
                if(bodyElement!=null && bodyElement.getFirstChild()!=null){
                    bodyAdded = true;
                }
            }
        }
        if(!bodyAdded){
            parent.removeChild(element);
        }
        return parent;
    }

    protected void addAllAttributes( Element element,Item item) throws SQLException {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            String name =((ValueAsAttributeGenerator)atts).getName();
            String value =((ValueAsAttributeGenerator)atts).getValue();
            element.setAttribute(name, value);
        }
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Required
    public void setBodyElementGenerators(List bodyElementGenerators) {
        this.bodyElementGenerators = bodyElementGenerators;
    }

    public void setAttributes(List<HALSwordDataAttributeGenerator> attributes) {
        this.attributes = attributes;
    }

    public void setBitstreamAttributes(List<BitstreamDependentAttributeGenerator> bitstreamAttributes) {
        this.bitstreamAttributes = bitstreamAttributes;
    }

    public void setDisabledFilter(List<DisabledElementFilter> disabledFilter){
        this.disabledFilter = disabledFilter;
    }

}
