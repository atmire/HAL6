package com.atmire.dspace.hal.v3;

import com.atmire.dspace.hal.Structure;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

public class UnvalidatedStructureStructure extends SolrStructure {

    public UnvalidatedStructureStructure() {
        super();
    }

    public SolrQuery basicSolrQuery() {
        SolrQuery solrQuery = new SolrQuery();
        String query = "*:*";
        solrQuery.setQuery(query);

        return solrQuery;
    }

    protected Structure responseToStructure(SolrDocument result) {
        Integer docid = (Integer) result.getFirstValue("docid");
        String label_s = (String) result.getFirstValue("label_s");
        return new Structure(docid, label_s);
    }

    protected ResponseParser getProcessor() {
        return new HalSolrXMLResponseParser();
        // QESXMLResponseParser works with the wt=json kind of response: {"response":{"numFound":16888,"start":0,"docs":[{"docid":.....
    }
}
