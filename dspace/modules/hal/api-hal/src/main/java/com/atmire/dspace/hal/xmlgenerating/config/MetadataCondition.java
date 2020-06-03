package com.atmire.dspace.hal.xmlgenerating.config;

import org.dspace.content.MetadataValue;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/09/13
 * Time: 10:24
 */
public interface MetadataCondition {

    public boolean matchesCondiction(MetadataValue value);
}
