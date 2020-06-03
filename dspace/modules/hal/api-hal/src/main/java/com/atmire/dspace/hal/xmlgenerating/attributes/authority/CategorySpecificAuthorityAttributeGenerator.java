package com.atmire.dspace.hal.xmlgenerating.attributes.authority;

import com.atmire.authority.hal.HALAuthorityValue;
import com.atmire.dspace.hal.xmlgenerating.attributes.HALSwordDataAttributeGenerator;
import org.apache.commons.lang.StringUtils;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.service.CachedAuthorityService;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Element;

/**
 * Created by jonas - jonas@atmire.com on 11/01/2018.
 */
public class CategorySpecificAuthorityAttributeGenerator implements HALSwordDataAttributeGenerator {

    private String prefix;
    private String name;

    @Autowired
    CachedAuthorityService cachedAuthorityService;

    @Override
    public void addAttribute(MetadataValue metadataValue, Element element) {

        if (metadataValue != null && StringUtils.isNotBlank(metadataValue.getAuthority())) {
            String authority = metadataValue.getAuthority();
            Context context = new Context();
            AuthorityValue authorityValue = cachedAuthorityService.findCachedAuthorityValueByAuthorityID(context, authority);
            if (authorityValue != null) {
                // TODO Check authorityValue instancer
                String value =((HALAuthorityValue) authorityValue).getHalIdentifier();
                if (StringUtils.isNotBlank(value)) {
                    element.setAttribute(name, prefix + value);
                }
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }

}
