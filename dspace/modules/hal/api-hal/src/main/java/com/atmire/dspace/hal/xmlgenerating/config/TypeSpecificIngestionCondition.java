package com.atmire.dspace.hal.xmlgenerating.config;

import org.dspace.content.service.ItemService;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 16/04/2018.
 */
public class TypeSpecificIngestionCondition implements IngestionCondition {

    @Autowired
    ItemService itemService;

    private List<String> valuesToAllow = new ArrayList<>();
    private String path;
    private String attribute;

    @Override
    public boolean shouldBeIngested(Element root) throws JDOMException {
        Element element = (org.jdom.Element) XPath.selectSingleNode(root, path);
        if (element != null) {
            String type = element.getAttributeValue(attribute);
            if (valuesToAllow.contains(type)) {
                return true;
            }
        }
        return false;
    }

    public void setValuesToAllow(List<String> valuesToAllow) {
        this.valuesToAllow = valuesToAllow;
    }

    @Required
    public void setPath(String path) {
        this.path = path;
    }
    @Required
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
