package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import com.atmire.authority.hal.HALAuthorityValue;
import org.apache.commons.lang.StringUtils;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.service.CachedAuthorityService;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * User: kevinvdv (kevin at atmire.com)
 * Date: 15/01/18
 * Time: 15:35
 */
public class MultiValueAuthorityMetadataFieldGenerator extends MultiValueMetadataFieldGenerator {

    @Autowired
    CachedAuthorityService cachedAuthorityService;

    public void attachMetadataValue(Document doc, Element element, Item item, MetadataValue metadataValue) {
        String authority = metadataValue.getAuthority();
        Context context = new Context();
        AuthorityValue authorityValue = cachedAuthorityService.findCachedAuthorityValueByAuthorityID(context, authority);

        if (authorityValue != null) {
            String halIdentifier =((HALAuthorityValue) authorityValue).getHalIdentifier();
            if (StringUtils.isNotBlank(halIdentifier)) {
                element.appendChild(doc.createTextNode(halIdentifier));
            }
        }
    }
}
