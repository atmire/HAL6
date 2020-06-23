package com.atmire.dspace.hal.xmlgenerating.elements.single;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.attributes.HalSwordStringDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 30/10/17.
 */
public class SingleLineMultiAttributeMetadataFieldGenerator implements HalSwordDataElementGenerator {

    protected String metadataField;
    private String elementName;
    private String attributename;
    protected List<HalSwordStringDataAttributeGenerator> attributes = new ArrayList<>();


    @Override
    public Element generateElement(Document doc, Element parent, Item item) throws SQLException {
        if (StringUtils.isNotBlank(metadataField)) {
            String value = getValue(item);

            if (StringUtils.isNotBlank(value) && CollectionUtils.isNotEmpty(attributes)) {
                Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
                parent.appendChild(element);
                addAllAttributes(value, element);
            }
        }

        return parent;
    }

    protected String getValue(Item item) {
        return item.getItemService().getMetadata(item, metadataField);
    }


    private void addAllAttributes(String dcv, Element element) {
        if(StringUtils.isNotBlank(attributename)){
            element.setAttribute(attributename,dcv);
        }
        for (HalSwordStringDataAttributeGenerator atts : attributes) {
            atts.addAttribute( element);
        }
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    public void setAttributename(String attributename) {
        this.attributename = attributename;
    }

    public void setMetadataField(String metadataField) {
        this.metadataField = metadataField;
    }

    public void setAttributes(List<HalSwordStringDataAttributeGenerator> attributes) {
        this.attributes = attributes;
    }

}
