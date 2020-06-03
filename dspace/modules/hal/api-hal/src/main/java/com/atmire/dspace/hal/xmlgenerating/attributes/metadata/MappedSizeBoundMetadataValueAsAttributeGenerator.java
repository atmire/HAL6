package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.MetadataValue;

import java.util.Map;

/**
 * Created by jonas - jonas@atmire.com on 14/03/2018.
 */
public class MappedSizeBoundMetadataValueAsAttributeGenerator extends SizeBoundMetadataValueAsAttributeGenerator {

    private Map<String, String> map;

    protected String getValue(MetadataValue dcValue) {
        String value = dcValue.getValue();
        if(map!=null && map.containsKey(value)){
            return map.get(value);
        }else if (StringUtils.isNotBlank(defaultValue)){
            return defaultValue;
        }
        return value;
    }

    public void setMap(Map<String,String> map){
        this.map = map;
    }
}
