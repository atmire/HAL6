package com.atmire.dspace.hal.v3;

import org.apache.solr.client.solrj.impl.XMLResponseParser;

/**
 * User: kevinvdv (kevin at atmire.com)
 * Date: 12/12/17
 * Time: 09:45
 */
public class HalSolrXMLResponseParser extends XMLResponseParser
{
    @Override
    public String getContentType() {
        return "text/xml; charset=UTF-8";
    }

}
