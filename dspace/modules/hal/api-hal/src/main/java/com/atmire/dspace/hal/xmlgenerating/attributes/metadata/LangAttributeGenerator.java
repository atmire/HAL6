package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Element;

/**
 * Created by jonas on 27/04/15.
 */
public class LangAttributeGenerator implements HALSwordDataAttributeGenerator {

    @Override
    public void addAttribute(MetadataValue dcValue, Element element) {

        if(StringUtils.isNotBlank(dcValue.getLanguage()))element.setAttribute("xml:lang", dcValue.getLanguage());
    }



}
