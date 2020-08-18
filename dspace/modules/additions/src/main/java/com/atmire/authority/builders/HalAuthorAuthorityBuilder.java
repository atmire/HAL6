package com.atmire.authority.builders;

import com.atmire.authority.author.HalAuthorAuthorityValue;
import com.atmire.dspace.hal.v3.HalSolrXMLResponseParser;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.content.DCPersonName;

import java.util.LinkedList;
import java.util.List;

public class HalAuthorAuthorityBuilder extends AbstractHalSolrAuthorityBuilder<HalAuthorAuthorityValue>{

    private static final Logger log = Logger.getLogger(HalAuthorAuthorityBuilder.class);

    public HalAuthorAuthorityValue buildAuthorityValueFromExternal(SolrDocument document) {

        HalAuthorAuthorityValue halAuthorAuthorityValue = buildAuthorityValue();

        String importedDocid = String.valueOf(document.getFieldValue("docid"));
        if(StringUtils.isNotBlank(importedDocid) && !StringUtils.equals(importedDocid, "null")){
            halAuthorAuthorityValue.setDocid(importedDocid);
        }

        String importedIdHal = String.valueOf(document.getFieldValue("idHal_i"));
        if(StringUtils.isNotBlank(importedIdHal) && !StringUtils.equals(importedIdHal, "null")){
            halAuthorAuthorityValue.setIdHal(importedIdHal);
        }

        String importedIdHalString = String.valueOf(document.getFieldValue("idHal_s"));
        if(StringUtils.isNotBlank(importedIdHalString) && !StringUtils.equals(importedIdHalString, "null")){
            halAuthorAuthorityValue.setIdHalString(importedIdHalString);
        }

        String importedLastname = String.valueOf(document.getFieldValue("lastName_s"));
        if(StringUtils.isNotBlank(importedLastname) && !StringUtils.equals(importedLastname, "null")){
            halAuthorAuthorityValue.setLastName(importedLastname);
        }

        String importedFirstname = String.valueOf(document.getFieldValue("firstName_s"));
        if(StringUtils.isNotBlank(importedFirstname) && !StringUtils.equals(importedFirstname, "null")){
            halAuthorAuthorityValue.setFirstName(importedFirstname);
        }

        halAuthorAuthorityValue.setValue(new DCPersonName(halAuthorAuthorityValue.getLastName(), halAuthorAuthorityValue.getFirstName()).toString());

        return halAuthorAuthorityValue;
    }

    public HalAuthorAuthorityValue buildAuthorityValue(SolrDocument document) {

        HalAuthorAuthorityValue halAuthorAuthorityValue = super.buildAuthorityValue(document);

        List<String> listDocId = ListUtils.emptyIfNull((List) document.getFieldValue("label_docid"));
        if(!listDocId.isEmpty() && StringUtils.isNotBlank(listDocId.get(0)) && !StringUtils.equals(listDocId.get(0), "null") ){
            halAuthorAuthorityValue.setDocid(listDocId.get(0));
        }

        List<String> listIdHal = ListUtils.emptyIfNull((List) document.getFieldValue("label_idhal"));
        if(!listIdHal.isEmpty() && StringUtils.isNotBlank(listIdHal.get(0)) && !StringUtils.equals(listIdHal.get(0), "null") ){
            halAuthorAuthorityValue.setIdHal(listIdHal.get(0));
        }
        List<String> listIdHalString = ListUtils.emptyIfNull((List) document.getFieldValue("label_idhalString"));
        if(!listIdHalString.isEmpty() && StringUtils.isNotBlank(listIdHalString.get(0)) && !StringUtils.equals(listIdHalString.get(0), "null") ){
            halAuthorAuthorityValue.setIdHalString(listIdHalString.get(0));
        }

        return halAuthorAuthorityValue;
    }
    @Override
    public List<HalAuthorAuthorityValue> buildAuthorityValueFromExternalSource(String text, int max) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(text);
            solrQuery.setRows(max);
            solrQuery.setParam("fl", "*");
            solrQuery.setFilterQueries("valid_s:VALID");
            SolrDocumentList solrDocumentList = solr.query(solrQuery).getResults();
            List<HalAuthorAuthorityValue> list = new LinkedList<>();
            for(SolrDocument solrDocument : solrDocumentList){
                HalAuthorAuthorityValue authorityValue = buildAuthorityValueFromExternal(solrDocument);
                list.add(authorityValue);
            }
            return list;
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

    public HalAuthorAuthorityValue buildAuthorityValue() {
        return new HalAuthorAuthorityValue();
    }

    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(authorityValueType, HalAuthorAuthorityValue.TYPE);

    }

    public void afterPropertiesSet() throws Exception {
        HttpSolrServer server = null;

        if (configurationService.getProperty("hal.solr-dc-contributor-author") != null)
        {
            try
            {
                server = new HttpSolrServer(configurationService.getProperty("hal.solr-dc-contributor-author"));
                server.setParser(new HalSolrXMLResponseParser());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        this.solr = server;

    }


}
