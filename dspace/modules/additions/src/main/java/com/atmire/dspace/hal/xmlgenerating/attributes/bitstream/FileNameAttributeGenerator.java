package com.atmire.dspace.hal.xmlgenerating.attributes.bitstream;

import org.dspace.content.Bitstream;
import org.w3c.dom.Element;

import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public class FileNameAttributeGenerator extends AbstractBitstreamAttributeGenerator implements BitstreamDependentAttributeGenerator {

    @Override
    public void addAttribute(Bitstream bitstream, Element element) throws SQLException {
        if(bitstream!=null){
           element.setAttribute(attributeName,bitstream.getName());
        }
    }
}
