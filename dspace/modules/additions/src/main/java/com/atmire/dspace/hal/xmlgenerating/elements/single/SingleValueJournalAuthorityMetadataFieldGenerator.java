package com.atmire.dspace.hal.xmlgenerating.elements.single;

import com.atmire.authority.journal.HalJournalAuthorityValue;
import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.apache.commons.lang.StringUtils;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.service.CachedAuthorityService;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 06/02/2018.
 */
public class SingleValueJournalAuthorityMetadataFieldGenerator extends  SingleValueMetadataFieldGenerator implements HalSwordDataElementGenerator {

    @Autowired
    CachedAuthorityService cachedAuthorityService;

    public Element generateElement(Document doc, Element parent, Item item) {
        if(condition!=null){
            if(!condition.exists(item)){
                return parent;
            }
        }
        List<MetadataValue> dcValues = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(metadataField)) {
            dcValues = item.getItemService().getMetadataByMetadataString(item, metadataField);
        }

        if(dcValues!=null && dcValues.size()>0 && org.apache.commons.lang3.StringUtils.isNotBlank(dcValues.get(0).getValue())){
            MetadataValue dcv = dcValues.get(0);
            Element element = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, elementName);
            parent.appendChild(element);
            addAllAttributes(dcv, element);
            boolean added = attachAuthorityMetadataValue(doc, element, item, dcv);
            if(!added){
                parent.removeChild(element);
            }
        }
        return parent;
    }

    //append a textnode based on the authority value to the element
    public boolean attachAuthorityMetadataValue(Document doc, Element element, Item item, MetadataValue dcValue) {
        Context context = new Context();
        AuthorityValue authorityValue = cachedAuthorityService.findCachedAuthorityValueByAuthorityID(context, dcValue.getAuthority());
        if (authorityValue != null) {
            if(authorityValue instanceof HalJournalAuthorityValue){
                String value =((HalJournalAuthorityValue) authorityValue).getDocid();
                if (StringUtils.isNotBlank(value)) {
                    element.appendChild(doc.createTextNode(value));
                    return true;
                }
            }
        }
        return false;

    }
}
