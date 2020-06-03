package com.atmire.dspace.hal.xmlgenerating.attributes.authority;

import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Element;

/**
 * Created by jonas on 27/04/15.
 */
public class AuthorityAttributeGenerator implements HALSwordDataAttributeGenerator {


    private String defaultAuthority;
    @Override
    public void addAttribute(MetadataValue metadataValue, Element element) {
        if (StringUtils.isNotBlank(defaultAuthority)) {
            element.setAttribute("role", defaultAuthority);
        } else {
            element.setAttribute("role", metadataValue.getAuthority());
        }
    }

    public void setDefaultAuthority(String defaultAuthority){
        this.defaultAuthority=defaultAuthority;
    }


}
