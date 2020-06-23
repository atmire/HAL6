package com.atmire.dspace.hal.v3;

import com.atmire.dspace.hal.Structure;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;

/**
 * Created by: Antoine Snyers (antoine at atmire dot com)
 * Date: 07 Oct 2014
 */
public class AuthorStructure extends SolrStructure {

    public AuthorStructure() {
        super();

    }

    public SolrQuery basicSolrQuery() {
        SolrQuery solrQuery = new SolrQuery();

        solrQuery.setParam("firstName_t", "*");
        solrQuery.setParam("lastName_t", "*");
        solrQuery.setParam("wt", "xml");

        return solrQuery;
    }

    protected Structure responseToStructure(SolrDocument result) {

        String docid = (String) result.getFirstValue("docid"); // the "docid" key has been forged in AuthorStructureResponseParser
        if (docid.startsWith("struct-")) {
            docid = docid.substring("struct-".length());
        }
        int id = Integer.valueOf(docid);
        Object orgName = result.getFieldValue("orgName");
        String label_s = null;
        if (orgName instanceof ArrayList) {
            ArrayList orgNames = (ArrayList) orgName;
            label_s = orgNames.get(0) + " [" + orgNames.get(1) + "]";
        } else{
            label_s = (String) orgName;
        }
        return new Structure(id, label_s);
    }

    protected ResponseParser getProcessor() {
        return new AuthorStructureResponseParser(); // a hack of XMLResponseParser...
        // XMLResponseParser works with the wt=json kind of response: {"response":{"numFound":16888,"start":0,"docs":[{"docid":.....
    }

}
