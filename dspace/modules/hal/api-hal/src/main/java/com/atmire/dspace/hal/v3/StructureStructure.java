package com.atmire.dspace.hal.v3;

import com.atmire.dspace.hal.Structure;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

/**
 * Created by: Antoine Snyers (antoine at atmire dot com)
 * Date: 07 Oct 2014
 */
public class StructureStructure extends SolrStructure {


    public StructureStructure() {
        super();
    }

    public SolrQuery basicSolrQuery() {
        SolrQuery solrQuery = new SolrQuery();
        String query = "*:*";
        solrQuery.setQuery(query);

        String[] filterQueries = commonFilterQueries();
        for (String filterQuery : filterQueries) {
            solrQuery.addFilterQuery(filterQuery);
        }
        return solrQuery;
    }

    protected String[] commonFilterQueries() {
        return new String[]{
                "valid_s:VALID",
                "type_s:laboratory"
        };
    }

    protected ResponseParser getProcessor() {
        return new HalSolrXMLResponseParser();
        // QESXMLResponseParser works with the wt=json kind of response: {"response":{"numFound":16888,"start":0,"docs":[{"docid":.....
    }

    protected Structure responseToStructure(SolrDocument result) {
        Integer docid = (Integer) result.getFirstValue("docid");
        String label_s = (String) result.getFirstValue("label_s");
        return new Structure(docid, label_s);
    }

}
