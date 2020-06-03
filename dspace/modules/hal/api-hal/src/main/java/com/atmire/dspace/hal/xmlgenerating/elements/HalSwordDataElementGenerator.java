package com.atmire.dspace.hal.xmlgenerating.elements;

import org.dspace.content.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;

/**
 * Created by jonas on 24/04/15.
 */
public interface HalSwordDataElementGenerator {


    public Element generateElement(Document doc, Element Parent, Item item) throws SQLException;
}
