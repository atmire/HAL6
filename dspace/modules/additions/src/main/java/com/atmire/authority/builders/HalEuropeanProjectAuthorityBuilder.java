package com.atmire.authority.builders;

import com.atmire.authority.project.HalEuropeanProjectAuthorityValue;
import com.atmire.dspace.hal.v3.HalSolrXMLResponseParser;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.dspace.authority.AuthorityCategory;

import java.util.List;

public class HalEuropeanProjectAuthorityBuilder extends AbstractHalSolrAuthorityBuilder<HalEuropeanProjectAuthorityValue>{


    private static final Logger log = Logger.getLogger(HalEuropeanProjectAuthorityBuilder.class);


    public HalEuropeanProjectAuthorityValue buildAuthorityValueFromExternal(SolrDocument document){

        HalEuropeanProjectAuthorityValue halEuropeanProjectAuthorityValue = buildAuthorityValue();


        String importedDocid = String.valueOf(document.getFieldValue("docid"));
        if(StringUtils.isNotBlank(importedDocid) && !StringUtils.equals(importedDocid, "null")){
            halEuropeanProjectAuthorityValue.setDocid(importedDocid);
        }


        String importedTitle = String.valueOf(document.getFieldValue("title_s"));
        if(StringUtils.isNotBlank(importedTitle) && !StringUtils.equals(importedTitle, "null")){
            halEuropeanProjectAuthorityValue.setTitle(importedTitle);
            halEuropeanProjectAuthorityValue.setValue(importedTitle);
        } else{
            halEuropeanProjectAuthorityValue.setValue("/");
        }

        String importedReference = String.valueOf(document.getFieldValue("reference_s"));
        if(StringUtils.isNotBlank(importedReference) && !StringUtils.equals(importedReference, "null")){
            halEuropeanProjectAuthorityValue.setReference(importedReference);
        }

        String importedFinance = String.valueOf(document.getFieldValue("financing_s"));
        if(StringUtils.isNotBlank(importedFinance) && !StringUtils.equals(importedFinance, "null")){
            halEuropeanProjectAuthorityValue.setFinance(importedFinance);
        }


        String importedValid = String.valueOf(document.getFieldValue("valid_s"));
        if(StringUtils.isNotBlank(importedValid) && !StringUtils.equals(importedValid, "null")){
            halEuropeanProjectAuthorityValue.setValid(importedValid);
        }

        String importedStartDate = String.valueOf(document.getFieldValue("startDate_s"));
        if(StringUtils.isNotBlank(importedStartDate) && !StringUtils.equals(importedStartDate, "null")){
            halEuropeanProjectAuthorityValue.setStartDate(importedStartDate);
        }

        String importedEndDate = String.valueOf(document.getFieldValue("endDate_s"));
        if(StringUtils.isNotBlank(importedEndDate) && !StringUtils.equals(importedEndDate, "null")){
            halEuropeanProjectAuthorityValue.setEndDate(importedEndDate);
        }

        return halEuropeanProjectAuthorityValue;
    }

    public HalEuropeanProjectAuthorityValue buildAuthorityValue(SolrDocument document){

        HalEuropeanProjectAuthorityValue halEuropeanProjectAuthorityValue = super.buildAuthorityValue(document);

        List<String> listDocId = ListUtils.emptyIfNull((List) document.getFieldValue("label_docid"));
        if(!listDocId.isEmpty() && StringUtils.isNotBlank(listDocId.get(0)) && !StringUtils.equals(listDocId.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setDocid(listDocId.get(0));
        }


        List<String> importedTitle = ListUtils.emptyIfNull((List) document.getFieldValue("label_title"));
        if(!importedTitle.isEmpty() && StringUtils.isNotBlank(importedTitle.get(0)) && !StringUtils.equals(importedTitle.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setTitle(importedTitle.get(0));
            halEuropeanProjectAuthorityValue.setValue(importedTitle.get(0));
        }

        List<String> importedReference = ListUtils.emptyIfNull((List) document.getFieldValue("label_reference"));
        if(!importedReference.isEmpty() && StringUtils.isNotBlank(importedReference.get(0)) && !StringUtils.equals(importedReference.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setReference(importedReference.get(0));
        }

        List<String> importedFinance = ListUtils.emptyIfNull((List) document.getFieldValue("label_finance"));
        if(!importedFinance.isEmpty() && StringUtils.isNotBlank(importedFinance.get(0)) && !StringUtils.equals(importedFinance.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setFinance(importedFinance.get(0));
        }

        List<String> importedValid = ListUtils.emptyIfNull((List) document.getFieldValue("label_valid"));
        if(!importedValid.isEmpty() && StringUtils.isNotBlank(importedValid.get(0)) && !StringUtils.equals(importedValid.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setValid(importedValid.get(0));
        }

        List<String> importedStartDate = ListUtils.emptyIfNull((List) document.getFieldValue("label_startdate"));
        if(!importedStartDate.isEmpty() && StringUtils.isNotBlank(importedStartDate.get(0)) && !StringUtils.equals(importedStartDate.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setStartDate(importedStartDate.get(0));
        }

        List<String> importedEndDate = ListUtils.emptyIfNull((List) document.getFieldValue("label_enddate"));
        if(!importedEndDate.isEmpty() && StringUtils.isNotBlank(importedEndDate.get(0)) && !StringUtils.equals(importedEndDate.get(0), "null") ){
            halEuropeanProjectAuthorityValue.setEndDate(importedEndDate.get(0));
        }

        return halEuropeanProjectAuthorityValue;
    }


    public HalEuropeanProjectAuthorityValue buildAuthorityValue() {
        return new HalEuropeanProjectAuthorityValue();
    }

    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(authorityValueType, AuthorityCategory.EUROPEAN_PROJECT.toString());
    }

    public void afterPropertiesSet() throws Exception {
        HttpSolrServer server = null;

        if (configurationService.getProperty("hal.solr-hal-funder-europe") != null)
        {
            try
            {
                server = new HttpSolrServer(configurationService.getProperty("hal.solr-hal-funder-europe"));
                server.setParser(new HalSolrXMLResponseParser());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        this.solr = server;

    }
}
