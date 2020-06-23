package com.atmire.authority.builders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.builders.ExternalSourceAuthorityValueBuilder;
import org.dspace.authority.factory.AuthorityValueBuilder;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractHalSolrAuthorityBuilder<T extends AuthorityValue> extends AuthorityValueBuilder<T> implements ExternalSourceAuthorityValueBuilder, InitializingBean {

    protected SolrServer solr;

    @Autowired(required = true)
    protected ConfigurationService configurationService;
    private static final Logger log = Logger.getLogger(AbstractHalSolrAuthorityBuilder.class);


    public List<T> buildAuthorityValueFromExternalSource(String text, int max) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery(text);
            solrQuery.setRows(max);
            solrQuery.setParam("fl", "*");
            SolrDocumentList solrDocumentList = solr.query(solrQuery).getResults();
            List<T> list = new LinkedList();
            for(SolrDocument solrDocument : solrDocumentList){
                T authorityValue = buildAuthorityValueFromExternal(solrDocument);
                list.add(authorityValue);
            }
            return list;
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

    public T buildAuthorityValue(String identifier, String content) {
        if(StringUtils.isBlank(identifier))
        {
            return null;
        }
        try {
            SolrQuery solrQuery = new SolrQuery();
            if(StringUtils.isNotBlank(identifier)){
                solrQuery.setQuery("docid:" + identifier);
            } else{
                return null;
            }
            // Make sure to retrieve all the fields
            solrQuery.setParam("fl", "*");
            QueryResponse response = solr.query(solrQuery);
            if(response!= null && response.getResults()!=null && response.getResults().get(0) != null){
                return buildAuthorityValueFromExternal(response.getResults().get(0));
            }
        } catch (SolrServerException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public abstract T buildAuthorityValueFromExternal(SolrDocument document);

    public abstract void afterPropertiesSet() throws Exception;

}
