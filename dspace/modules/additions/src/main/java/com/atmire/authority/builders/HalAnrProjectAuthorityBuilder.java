package com.atmire.authority.builders;

import com.atmire.authority.project.HalAnrProjectAuthorityValue;
import com.atmire.dspace.hal.v3.HalSolrXMLResponseParser;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.authority.AuthorityCategory;

import java.util.LinkedList;
import java.util.List;

public class HalAnrProjectAuthorityBuilder extends AbstractHalSolrAuthorityBuilder<HalAnrProjectAuthorityValue>{


    private static final Logger log = Logger.getLogger(HalAnrProjectAuthorityBuilder.class);


    public HalAnrProjectAuthorityValue buildAuthorityValueFromExternal(SolrDocument document){

        HalAnrProjectAuthorityValue halAnrProjectAuthorityValue = buildAuthorityValue();


        String importedDocid = String.valueOf(document.getFieldValue("docid"));
        if(StringUtils.isNotBlank(importedDocid) && !StringUtils.equals(importedDocid, "null")){
            halAnrProjectAuthorityValue.setDocid(importedDocid);
        }

        String importedTitle = String.valueOf(document.getFieldValue("title_s"));
        if(StringUtils.isNotBlank(importedTitle) && !StringUtils.equals(importedTitle, "null")){
            halAnrProjectAuthorityValue.setTitle(importedTitle);
            halAnrProjectAuthorityValue.setValue(importedTitle);
        }
        else{
            halAnrProjectAuthorityValue.setValue("/");
        }

        String importedAcronym = String.valueOf(document.getFieldValue("acronym_s"));
        if(StringUtils.isNotBlank(importedAcronym) && !StringUtils.equals(importedAcronym, "null")){
            halAnrProjectAuthorityValue.setAcronym(importedAcronym);
        }
        String importedReference = String.valueOf(document.getFieldValue("reference_s"));
        if(StringUtils.isNotBlank(importedReference) && !StringUtils.equals(importedReference, "null")){
            halAnrProjectAuthorityValue.setReference(importedReference);
        }

        String importedCallTitle = String.valueOf(document.getFieldValue("callTitle_s"));
        if(StringUtils.isNotBlank(importedCallTitle) && !StringUtils.equals(importedCallTitle, "null")){
            halAnrProjectAuthorityValue.setCallTitle(importedCallTitle);
        }

        String importedCallAcronym = String.valueOf(document.getFieldValue("callAcronym_s"));
        if(StringUtils.isNotBlank(importedCallAcronym) && !StringUtils.equals(importedCallAcronym, "null")){
            halAnrProjectAuthorityValue.setCallAcronym(importedCallAcronym);
        }

        String importedValid = String.valueOf(document.getFieldValue("valid_s"));
        if(StringUtils.isNotBlank(importedValid) && !StringUtils.equals(importedValid, "null")){
            halAnrProjectAuthorityValue.setValid(importedValid);
        }

        String importedYearDate = String.valueOf(document.getFieldValue("yearDate_s"));
        if(StringUtils.isNotBlank(importedYearDate) && !StringUtils.equals(importedYearDate, "null")){
            halAnrProjectAuthorityValue.setYearDate(importedYearDate);
        }

        return halAnrProjectAuthorityValue;
    }

    public HalAnrProjectAuthorityValue buildAuthorityValue(SolrDocument document){

        HalAnrProjectAuthorityValue halAnrProjectAuthorityValue = super.buildAuthorityValue(document);

        List<String> listDocId = ListUtils.emptyIfNull((List) document.getFieldValue("label_docid"));
        if(!listDocId.isEmpty() && StringUtils.isNotBlank(listDocId.get(0)) && !StringUtils.equals(listDocId.get(0), "null") ){
            halAnrProjectAuthorityValue.setDocid(listDocId.get(0));
        }


        List<String> importedTitle = ListUtils.emptyIfNull((List) document.getFieldValue("label_title"));
        if(!importedTitle.isEmpty() && StringUtils.isNotBlank(importedTitle.get(0)) && !StringUtils.equals(importedTitle.get(0), "null") ){
            halAnrProjectAuthorityValue.setTitle(importedTitle.get(0));
            halAnrProjectAuthorityValue.setValue(importedTitle.get(0));
        }
        else{
            halAnrProjectAuthorityValue.setValue("/");
        }

        List<String> importedAcronym = ListUtils.emptyIfNull((List) document.getFieldValue("label_acronym"));
        if(!importedAcronym.isEmpty() && StringUtils.isNotBlank(importedAcronym.get(0)) && !StringUtils.equals(importedAcronym.get(0), "null") ){
            halAnrProjectAuthorityValue.setAcronym(importedAcronym.get(0));
        }


        List<String> importedReference = ListUtils.emptyIfNull((List) document.getFieldValue("label_reference"));
        if(!importedReference.isEmpty() && StringUtils.isNotBlank(importedReference.get(0)) && !StringUtils.equals(importedReference.get(0), "null") ){
            halAnrProjectAuthorityValue.setReference(importedReference.get(0));
        }

        List<String> importedCallTitle = ListUtils.emptyIfNull((List) document.getFieldValue("label_calltitle"));
        if(!importedCallTitle.isEmpty() && StringUtils.isNotBlank(importedCallTitle.get(0)) && !StringUtils.equals(importedCallTitle.get(0), "null") ){
            halAnrProjectAuthorityValue.setCallTitle(importedCallTitle.get(0));
        }
        List<String> importedCallAcronym = ListUtils.emptyIfNull((List) document.getFieldValue("label_callacronym"));
        if(!importedCallAcronym.isEmpty() && StringUtils.isNotBlank(importedCallAcronym.get(0)) && !StringUtils.equals(importedCallAcronym.get(0), "null") ){
            halAnrProjectAuthorityValue.setCallAcronym(importedCallAcronym.get(0));
        }

        List<String> importedValid = ListUtils.emptyIfNull((List) document.getFieldValue("label_valid"));
        if(!importedValid.isEmpty() && StringUtils.isNotBlank(importedValid.get(0)) && !StringUtils.equals(importedValid.get(0), "null") ){
            halAnrProjectAuthorityValue.setValid(importedValid.get(0));
        }

        List<String> importedYearDate = ListUtils.emptyIfNull((List) document.getFieldValue("label_yeardate"));
        if(!importedYearDate.isEmpty() && StringUtils.isNotBlank(importedYearDate.get(0)) && !StringUtils.equals(importedYearDate.get(0), "null") ){
            halAnrProjectAuthorityValue.setYearDate(importedYearDate.get(0));
        }

        return halAnrProjectAuthorityValue;
    }


    public HalAnrProjectAuthorityValue buildAuthorityValue() {
        return new HalAnrProjectAuthorityValue();
    }

    @Override
    public List<HalAnrProjectAuthorityValue> buildAuthorityValueFromExternalSource(String text, int max) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(text);
            solrQuery.setRows(max);
            solrQuery.setParam("fl", "*");
            solrQuery.setFilterQueries("valid_s:VALID");
            SolrDocumentList solrDocumentList = solr.query(solrQuery).getResults();
            List<HalAnrProjectAuthorityValue> list = new LinkedList();
            for(SolrDocument solrDocument : solrDocumentList){
                HalAnrProjectAuthorityValue authorityValue = buildAuthorityValueFromExternal(solrDocument);
                list.add(authorityValue);
            }
            return list;
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(authorityValueType, AuthorityCategory.ANR_PROJECT.toString());
    }

    public void afterPropertiesSet() throws Exception {
        HttpSolrServer server = null;

        if (configurationService.getProperty("solr-hal-funder-anr") != null)
        {
            try
            {
                server = new HttpSolrServer(configurationService.getProperty("solr-hal-funder-anr"));
                server.setParser(new HalSolrXMLResponseParser());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        this.solr = server;

    }
}
