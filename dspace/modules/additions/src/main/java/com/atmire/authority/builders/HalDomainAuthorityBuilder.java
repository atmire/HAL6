package com.atmire.authority.builders;

import com.atmire.authority.domain.HalDomainAuthorityValue;
import com.atmire.dspace.hal.v3.HalSolrXMLResponseParser;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.dspace.authority.AuthorityCategory;

import java.util.List;

public class HalDomainAuthorityBuilder  extends AbstractHalSolrAuthorityBuilder<HalDomainAuthorityValue> {

    private static final Logger log = Logger.getLogger(HalDomainAuthorityBuilder.class);


    public HalDomainAuthorityValue buildAuthorityValueFromExternal(SolrDocument document) {

        HalDomainAuthorityValue halDomainAuthorityValue = buildAuthorityValue();

        String importedDocid = String.valueOf(document.getFieldValue("docid"));
        if(StringUtils.isNotBlank(importedDocid) && !StringUtils.equals(importedDocid, "null")){
            halDomainAuthorityValue.setDocid(importedDocid);
        }

        String importedFrDomain = String.valueOf(document.getFieldValue("fr_domain_s"));
        if(StringUtils.isNotBlank(importedFrDomain) && !StringUtils.equals(importedFrDomain, "null")){
            halDomainAuthorityValue.setFrDomain(importedFrDomain);
            halDomainAuthorityValue.setValue(importedFrDomain);
        }

        String importedCode = String.valueOf(document.getFieldValue("code_s"));
        if(StringUtils.isNotBlank(importedCode) && !StringUtils.equals(importedCode, "null")){
            halDomainAuthorityValue.setCode(importedCode);
        }

        String importedLevel = String.valueOf(document.getFieldValue("level_i"));
        if(StringUtils.isNotEmpty(importedLevel) && !StringUtils.equals(importedLevel, "null")){
            halDomainAuthorityValue.setLevel(importedLevel);
        }

        String importedParent = String.valueOf(document.getFieldValue("parent_i"));
        if(StringUtils.isNotEmpty(importedParent) && !StringUtils.equals(importedParent, "null")){
            halDomainAuthorityValue.setParent(importedParent);
        }

        return halDomainAuthorityValue;
    }


    public HalDomainAuthorityValue buildAuthorityValue(SolrDocument document){
        HalDomainAuthorityValue halDomainAuthorityValue = super.buildAuthorityValue(document);

        List<String> listDocId = ListUtils.emptyIfNull((List) document.getFieldValue("label_docid"));
        if(!listDocId.isEmpty() && StringUtils.isNotBlank(listDocId.get(0)) && !StringUtils.equals(listDocId.get(0), "null") ){
            halDomainAuthorityValue.setDocid(listDocId.get(0));
        }

        List<String> listFrDomain = ListUtils.emptyIfNull((List) document.getFieldValue("label_fr_domain"));
        if(!listFrDomain.isEmpty() && StringUtils.isNotBlank(listFrDomain.get(0)) && !StringUtils.equals(listFrDomain.get(0), "null") ){
            halDomainAuthorityValue.setFrDomain(listFrDomain.get(0));
            halDomainAuthorityValue.setValue(listFrDomain.get(0));
        }

        List<String> listCode = ListUtils.emptyIfNull((List) document.getFieldValue("label_code"));
        if(!listCode.isEmpty() && StringUtils.isNotBlank(listCode.get(0)) && !StringUtils.equals(listCode.get(0), "null") ){
            halDomainAuthorityValue.setCode(listCode.get(0));
        }

        List<String> listLevel = ListUtils.emptyIfNull((List) document.getFieldValue("label_level"));
        if(!listLevel.isEmpty() && StringUtils.isNotBlank(listLevel.get(0)) && !StringUtils.equals(listLevel.get(0), "null") ){
            halDomainAuthorityValue.setLevel(listLevel.get(0));
        }

        List<String> listParent = ListUtils.emptyIfNull((List) document.getFieldValue("label_parent"));
        if(!listParent.isEmpty() && StringUtils.isNotBlank(listParent.get(0)) && !StringUtils.equals(listParent.get(0), "null") ){
            halDomainAuthorityValue.setParent(listParent.get(0));
        }

        return halDomainAuthorityValue;
    }

    public HalDomainAuthorityValue buildAuthorityValue() {
        return new HalDomainAuthorityValue();
    }

    public void afterPropertiesSet() throws Exception {
        HttpSolrServer server = null;

        if (configurationService.getProperty("hal.solr-hal-domain") != null)
        {
            try
            {
                server = new HttpSolrServer(configurationService.getProperty("hal.solr-hal-domain"));
                server.setParser(new HalSolrXMLResponseParser());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        this.solr = server;
    }


    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(authorityValueType, AuthorityCategory.DOMAIN.toString());
    }
}
