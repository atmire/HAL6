package com.atmire.dspace.hal.xmlgenerating.elements.multi;

import com.atmire.authority.author.HalAuthorAuthorityValue;
import com.atmire.dspace.content.service.AtmireMetadataValueService;
import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.service.CachedAuthorityService;
import org.apache.log4j.Logger;
import org.dspace.content.DCPersonName;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataValue;
import org.dspace.content.service.MetadataFieldService;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jonas on 06/05/15.
 */
public class MultiValueAuthorGenerator extends MultiValueMetadataFieldGenerator {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(MultiValueAuthorGenerator.class);

    @Autowired
    AtmireMetadataValueService metadataValueService;
    @Autowired
    MetadataFieldService metadataFieldService;
    @Autowired
    CachedAuthorityService cachedAuthorityService;

    @Override
    public void attachMetadataValue(Document doc, Element parent, Item i, MetadataValue author) {
        DCPersonName name = new DCPersonName(author.getValue());
        String firstName = name.getFirstNames();
        String lastName = name.getLastName();
        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            populateXMLWithNameInfo(doc, parent, firstName, lastName);
        }

        Context context = new Context();

        addAuthorID(context, doc, parent, author);
        processChildValues(context, doc, parent, author);

    }

    private void addAuthorID(Context context,Document doc, Element parent, MetadataValue author) {
        String authority = author.getAuthority();
        if(StringUtils.isNotBlank(authority)){

            AuthorityValue authorityValue = cachedAuthorityService.findCachedAuthorityValueByAuthorityID(context, authority);
            if(authorityValue instanceof HalAuthorAuthorityValue){
                fillInHALIdentifiers(doc, parent, (HalAuthorAuthorityValue) authorityValue);
            }
        }
    }

    private void fillInHALIdentifiers(Document doc, Element parent, HalAuthorAuthorityValue authorityValue) {
        String halID = authorityValue.getIdHal();
        if(StringUtils.isNotBlank(halID)){
            Element halAuthorID = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "idno");
            halAuthorID.setAttribute("type", "idhal");
            halAuthorID.setAttribute("notation", "numeric");
            halAuthorID.setTextContent(halID);
            parent.appendChild(halAuthorID);
        }
        String docID = authorityValue.getDocid();
        if(StringUtils.isNotBlank(docID)){
            Element halAuthorID = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "idno");
            halAuthorID.setAttribute("type", "halauthorid");
            halAuthorID.setTextContent(docID);
            parent.appendChild(halAuthorID);
        }
        String halIDString = authorityValue.getIdHalString();
        if(StringUtils.isNotBlank(halIDString)){
            Element halAuthorID = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "idno");
            halAuthorID.setAttribute("type", "idhal");
            halAuthorID.setAttribute("notation", "string");
            halAuthorID.setTextContent(halIDString);
            parent.appendChild(halAuthorID);
        }
    }

    private void processChildValues(Context context,Document doc, Element parent, MetadataValue author) {
        try {

            MetadataField structureMetadataField = metadataFieldService.findByElement(context, "hal", "structure", "identifier");
            MetadataField halFunctionMetadataField = metadataFieldService.findByElement(context, "hal", "author", "function");
            List<MetadataValue> childValues = author.getChildMetadataValues();
            if (CollectionUtils.isNotEmpty(childValues)) {
                for (MetadataValue childValue : childValues) {
                    if (childValue.getMetadataField().equals(structureMetadataField)) {
                        Element affiliation = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "affiliation");
                        affiliation.setAttribute("ref", "#struct-" + childValue.getValue());
                        parent.appendChild(affiliation);
                    } else if (childValue.getMetadataField().equals(halFunctionMetadataField)) {
                        parent.setAttribute("role", childValue.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error while retrieving structure identifier/author function metadata field", e);
        }
    }

    private boolean isNotNullOrEmpty(int[] structureIdsValues) {
        return structureIdsValues!=null && structureIdsValues.length > 0;
    }


    private void populateXMLWithNameInfo(Document doc, Element parent, String firstName,String lastNames) {
        Element persName = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "persName");
        Element node1 = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "forename");
        node1.appendChild(doc.createTextNode(firstName));
        node1.setAttribute("type","first");
        Element node2 = doc.createElementNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE, "surname");
        node2.appendChild(doc.createTextNode(lastNames));

        persName.appendChild(node1);
        persName.appendChild(node2);
        parent.appendChild(persName);
    }
}