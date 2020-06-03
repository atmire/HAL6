package com.atmire.dspace.hal.xmlgenerating.elements.wrappers;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.attributes.metadata.ValueAsAttributeGenerator;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jonas on 18/05/15.
 */
public class ComboHalSwordEmbargoGenerator extends ComboHalSwordDataElementGenerator {

    public static Logger log = LoggerFactory.getLogger(ComboHalSwordEmbargoGenerator.class);

    @Override
    public Element generateElement(Document doc, Element parent,Item item) throws SQLException {
        List<Bundle> itemBundles = item.getItemService().getBundles(item,"ORIGINAL");
        boolean firstFile = true;
        for (Bundle itemBundle : itemBundles) {
            List<Bitstream> bitstreams = itemBundle.getBitstreams();
            for (Bitstream bitstream : bitstreams)
            {
                Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
                parent.appendChild(element);
                addAllAttributes(element, bitstream);

                if(firstFile){
                    element.setAttribute("type", "file");
                    firstFile=false;
                    element.setAttribute("n","1");
                }else{
                    element.setAttribute("type", "annex");
                    element.setAttribute("n","0");
                }
            }
        }

        return parent;
    }

    protected void addAllAttributes(Element element, Bitstream bitstream) {
        for (HALSwordDataAttributeGenerator atts : attributes) {
            String name =((ValueAsAttributeGenerator)atts).getName();
            String value =((ValueAsAttributeGenerator)atts).getValue();
            element.setAttribute(name, value);
        }
        addTargetAttribute(element,bitstream);
    }

    private void addTargetAttribute(Element element, Bitstream bitstream) {
        element.setAttribute("target", bitstream.getName());
    }


}
