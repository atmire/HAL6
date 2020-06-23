package com.atmire.dspace.hal.xmlgenerating.elements.wrappers;

import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 06/02/2018.
 */
public class PreferentialHalSwordDataElementGenerator implements HalSwordDataElementGenerator {

    private List<HalSwordDataElementGenerator> bodyElementGenerators;
    protected List<HALSwordDataAttributeGenerator> attributes = new ArrayList<>();

    public Element generateElement(Document doc, Element parent,Item item) throws SQLException {

        if (bodyElementGenerators != null) {
            for (HalSwordDataElementGenerator dataElementGenerator : bodyElementGenerators) {
                Element bodyElement = dataElementGenerator.generateElement(doc, parent, item);
                if (bodyElement != null && bodyElement.getFirstChild() != null) {
                    // The order of the bodyElements is set in the beans,
                    // if one is found, favor it over the next ones, stop immediately.
                    return parent;
                }
            }
        }
        return parent;
    }

    @Required
    public void setBodyElementGenerators(List bodyElementGenerators) {
        this.bodyElementGenerators = bodyElementGenerators;
    }

}
