package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonas - jonas@atmire.com on 09/02/2018.
 */
public class CreativeCommonsAttributeGenerator extends MetadataValueAsAttributeGenerator {

    private static final String PUBLIC_LICENSE = "http://creativecommons.org/publicdomain/zero/1.0/";
    private static final String NON_PUBLIC_LICENSE_REGEX = "http(s?)://creativecommons.org/(\\D)+";

    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {
        String value = dcValue.getValue();
        if (StringUtils.isNotBlank(dcValue.getValue())) {
            String trimmedValue = trimLicenseValue(value);
            element.setAttribute(attributeName, trimmedValue);
        }
    }

    private String trimLicenseValue(String value) {
        if(StringUtils.equalsIgnoreCase(PUBLIC_LICENSE,value)){
            return value;
        }
        Pattern pattern = Pattern.compile(NON_PUBLIC_LICENSE_REGEX);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find())
        {
            return matcher.group(0);
        }
        return value;
    }
}
