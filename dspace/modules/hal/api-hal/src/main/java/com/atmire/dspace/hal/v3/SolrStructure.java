package com.atmire.dspace.hal.v3;

import com.atmire.dspace.hal.Structure;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by: Antoine Snyers (antoine at atmire dot com)
 * Date: 07 Oct 2014
 */
public abstract class SolrStructure {

    protected String solrUrl;

     protected SolrStructure() {
    }

    public String getSolrUrl(){
        return solrUrl;
    }

    @Required
    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    /**
     * Non-Static HttpSolrServer
     */
    private HttpSolrServer solr = null;

    public HttpSolrServer getSolr() {
        if (solr == null) {

            UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
            if (urlValidator.isValid(getSolrUrl())) {
                try {
                    log.debug("Solr URL: " + getSolrUrl());
                    solr = new HttpSolrServer(getSolrUrl());
                    solr.setConnectionTimeout(30000);
                    solr.setSoTimeout(30000);
                    solr.setBaseURL(getSolrUrl());
                    solr.setUseMultiPartPost(true);
                    ResponseParser processor = getProcessor();
                    if (processor != null) {
                        solr.setParser(processor);
                    }

                    SolrQuery solrQuery = basicSolrQuery();
                    solr.query(solrQuery);
                } catch (SolrServerException e) {
                    log.error("Error while initializing solr server", e);
                    solr = null;
                }
            } else {
                log.error("Error while initializing solr, invalid url: " + getSolrUrl());
            }
        }

        return solr;
    }


    protected ResponseParser getProcessor() {
        return null;
    }

    public abstract SolrQuery basicSolrQuery();

    /**
     * log4j logger
     */
    private static Logger log = Logger.getLogger(SolrStructure.class);

    public List<Structure> responseToStructures(QueryResponse solrResponse) {
        List<Structure> structures = null;

        if (solrResponse != null) {
            structures = new ArrayList<Structure>();
            SolrDocumentList results = solrResponse.getResults();
            for (SolrDocument result : results) {
                Structure structure = responseToStructure(result);
                structures.add(structure);
            }
        }
        return structures;
    }

    protected abstract Structure responseToStructure(SolrDocument result);

    public QueryResponse query(SolrQuery solrQuery) {
        QueryResponse solrResponse = null;
        try {
            solrResponse = getSolr().query(solrQuery);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return solrResponse;
    }

    public List<Structure> queryStructures(Map<String, String> params, String sortField) {
        SolrQuery solrQuery = basicSolrQuery();
        insertParams(solrQuery, params);
        if(StringUtils.isNotBlank(sortField)){
            solrQuery.setSortField("docid", SolrQuery.ORDER.asc);
        }
        QueryResponse solrResponse = query(solrQuery);
        return responseToStructures(solrResponse);
    }

    public List<Structure> queryStructures(List<String> filterQueries, String sortField) {
        SolrQuery solrQuery = basicSolrQuery();
        insertParams(solrQuery, filterQueries);
        if(StringUtils.isNotBlank(sortField)){
            solrQuery.setSortField("docid", SolrQuery.ORDER.asc);
        }
        QueryResponse solrResponse = query(solrQuery);
        return responseToStructures(solrResponse);
    }

    protected void insertParams(SolrQuery solrQuery, List<String> params) {
        for (String param : params) {
            solrQuery.addFilterQuery(param);
        }
    }

    protected void insertParams(SolrQuery solrQuery, Map<String, String> params) {
        for (String param : params.keySet()) {
            String value = params.get(param);
            solrQuery.setParam(param, value);
        }
    }
}
