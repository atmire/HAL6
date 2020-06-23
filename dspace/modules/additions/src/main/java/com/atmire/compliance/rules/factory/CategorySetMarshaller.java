package com.atmire.compliance.rules.factory;

import com.atmire.compliance.definition.model.CategorySet;
import com.atmire.util.XmlMarshaller;

/**
 * XML marshaller to unmarshall or marshall the validation rule definition file
 */
public class CategorySetMarshaller extends XmlMarshaller<CategorySet> {

    public CategorySetMarshaller() {
        super(CategorySet.class);
    }

}
