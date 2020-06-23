package com.atmire.dspace.hal.xmlgenerating.attributes;

import org.dspace.content.MetadataValue;
import org.w3c.dom.Element;

/**
 * Created by jonas on 27/04/15.
 */
public interface HALSwordDataAttributeGenerator {

    public void addAttribute(MetadataValue metadataValue, Element element);

}
