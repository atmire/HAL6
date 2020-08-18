package com.atmire.authority.builders;

import com.atmire.authority.journal.HalJournalAuthorityValue;
import com.atmire.dspace.hal.v3.HalSolrXMLResponseParser;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.dspace.authority.AuthorityCategory;

import java.util.List;

public class HalJournalAuthorityBuilder extends AbstractHalSolrAuthorityBuilder<HalJournalAuthorityValue>{


    private static final Logger log = Logger.getLogger(HalJournalAuthorityBuilder.class);


    public HalJournalAuthorityValue buildAuthorityValueFromExternal(SolrDocument document){

        HalJournalAuthorityValue halJournalAuthorityValue = buildAuthorityValue();

        String importedDocid = String.valueOf(document.getFieldValue("docid"));
        if(StringUtils.isNotBlank(importedDocid) && !StringUtils.equals(importedDocid, "null")){
            halJournalAuthorityValue.setDocid(importedDocid);
        }

        String importedPublisher = String.valueOf(document.getFieldValue("publisher_s"));
        if(StringUtils.isNotBlank(importedPublisher) && !StringUtils.equals(importedPublisher, "null")){
            halJournalAuthorityValue.setPublisher(importedPublisher);
        }

        String importedIssn = String.valueOf(document.getFieldValue("issn_s"));
        if(StringUtils.isNotBlank(importedIssn) && !StringUtils.equals(importedIssn, "null")){
            halJournalAuthorityValue.setIssn(importedIssn);
        }

        String importedTitle = String.valueOf(document.getFieldValue("title_s"));
        if(StringUtils.isNotEmpty(importedTitle) && !StringUtils.equals(importedTitle, "null")){
            halJournalAuthorityValue.setTitle(importedTitle);
            halJournalAuthorityValue.setValue(importedTitle);
        }

        return halJournalAuthorityValue;
    }

    public HalJournalAuthorityValue buildAuthorityValue(SolrDocument document){

        HalJournalAuthorityValue halJournalAuthorityValue = super.buildAuthorityValue(document);

        List<String> listDocId = ListUtils.emptyIfNull((List) document.getFieldValue("label_docid"));
        if(!listDocId.isEmpty() && StringUtils.isNotBlank(listDocId.get(0)) && !StringUtils.equals(listDocId.get(0), "null") ){
            halJournalAuthorityValue.setDocid(listDocId.get(0));
        }

        List<String> listImportedPublisher = ListUtils.emptyIfNull((List) document.getFieldValue("label_publisher"));
        if(!listImportedPublisher.isEmpty() && StringUtils.isNotBlank(listImportedPublisher.get(0)) && !StringUtils.equals(listImportedPublisher.get(0), "null") ){
            halJournalAuthorityValue.setPublisher(listImportedPublisher.get(0));
        }

        List<String> listImportedIssn = ListUtils.emptyIfNull((List) document.getFieldValue("label_issn"));
        if(!listImportedIssn.isEmpty() && StringUtils.isNotBlank(listImportedIssn.get(0)) && !StringUtils.equals(listImportedIssn.get(0), "null") ){
            halJournalAuthorityValue.setIssn(listImportedIssn.get(0));
        }

        List<String> listImportedTitle = ListUtils.emptyIfNull((List) document.getFieldValue("label_title"));
        if(!listImportedTitle.isEmpty() && StringUtils.isNotBlank(listImportedTitle.get(0)) && !StringUtils.equals(listImportedTitle.get(0), "null") ){
            halJournalAuthorityValue.setTitle(listImportedTitle.get(0));
            halJournalAuthorityValue.setValue(listImportedTitle.get(0));
        }

        return halJournalAuthorityValue;
    }


    public HalJournalAuthorityValue buildAuthorityValue() {
        return new HalJournalAuthorityValue();
    }

    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(authorityValueType, AuthorityCategory.JOURNAL.toString());
    }

    public void afterPropertiesSet() throws Exception {
        HttpSolrServer server = null;

        if (configurationService.getProperty("hal.solr-hal-journal") != null)
        {
            try
            {
                server = new HttpSolrServer(configurationService.getProperty("hal.solr-hal-journal"));
                server.setParser(new HalSolrXMLResponseParser());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        this.solr = server;

    }
}
