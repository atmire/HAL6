package com.atmire.dspace.hal.xmlgenerating.bitstream;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.bitstream.BitstreamDependentAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.metadata.ValueAsAttributeGenerator;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 05/01/2018.
 */
public class BitstreamInfoGenerator implements HalSwordDataElementGenerator {

    private String elementName;
    protected List<HALSwordDataAttributeGenerator> attributes = new ArrayList<>();
    protected List<BitstreamDependentAttributeGenerator> bitstreamAttributes = new ArrayList<>();
    private List<BitstreamInfoGenerator> bodyElementGenerators;

    BitstreamRetrievalService bitstreamRetrievalService;

    @Override
    public Element generateElement(Document doc, Element parent, Item item) throws SQLException {
        if(bitstreamRetrievalService==null){
            return parent;
        }
        int bistreamCount = 0;
        List<Bitstream> bitstreams = bitstreamRetrievalService.retrieveBitstreams(item);
        for(Bitstream bitstream : bitstreams){
            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addAllAttributes(element, bitstream,bistreamCount);
            bistreamCount++;
            if (bodyElementGenerators != null) {
                for (BitstreamInfoGenerator dataElementGenerator : bodyElementGenerators) {
                    dataElementGenerator.generateElement(doc, element,bitstream);
                }
            }
        }
        return parent;
    }


    public Element generateElement(Document doc, Element parent, Bitstream bitstream) throws SQLException {
        Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
        parent.appendChild(element);
        addAllAttributes(element, bitstream, -1);
        return parent;
    }

    protected void addAllAttributes(Element element, Bitstream bitstream, int bitstreamCount) throws SQLException {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            String name =((ValueAsAttributeGenerator)atts).getName();
            String value =((ValueAsAttributeGenerator)atts).getValue();
            element.setAttribute(name, value);
        }
        // If -1 is passed along this is disabled
        if(bitstreamCount != -1)
        {
            final Bitstream primaryBitstream = bitstream.getBundles().get(0).getPrimaryBitstream();
            if(bitstream.equals(primaryBitstream) || (primaryBitstream == null && bitstreamCount == 0)){
                element.setAttribute("n", String.valueOf(1));
            }
        }
        for (BitstreamDependentAttributeGenerator atts : bitstreamAttributes) {
            atts.addAttribute(bitstream, element);
        }
    }

    @Required
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setAttributes(List<HALSwordDataAttributeGenerator> attributes) {
        this.attributes = attributes;
    }

    public void setBitstreamAttributes(List<BitstreamDependentAttributeGenerator> bitstreamAttributes) {
        this.bitstreamAttributes = bitstreamAttributes;
    }

    public void setBitstreamRetrievalService(BitstreamRetrievalService bitstreamRetrievalService){
        this.bitstreamRetrievalService = bitstreamRetrievalService;
    }

    public void setBodyElementGenerators(List bodyElementGenerators) {
        this.bodyElementGenerators = bodyElementGenerators;
    }
}
