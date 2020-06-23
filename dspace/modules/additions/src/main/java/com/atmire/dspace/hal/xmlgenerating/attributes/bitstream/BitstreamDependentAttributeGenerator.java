package com.atmire.dspace.hal.xmlgenerating.attributes.bitstream;

import org.dspace.content.Bitstream;
import org.w3c.dom.Element;

import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public interface BitstreamDependentAttributeGenerator {

    void addAttribute(Bitstream bitsream, Element element) throws SQLException;
}
