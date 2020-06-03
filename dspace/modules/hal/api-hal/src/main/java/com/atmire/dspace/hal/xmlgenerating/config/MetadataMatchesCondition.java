package com.atmire.dspace.hal.xmlgenerating.config;

import org.dspace.content.MetadataValue;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/09/13
 * Time: 10:25
 */
public class MetadataMatchesCondition implements MetadataCondition {


    private String regex;

    @Override
    public boolean matchesCondiction(MetadataValue value) {
        return value.getValue().matches(regex);
    }

    public String getRegex() {
        return regex;
    }

    @Required
    public void setRegex(String regex) {
        this.regex = regex;
    }
}
