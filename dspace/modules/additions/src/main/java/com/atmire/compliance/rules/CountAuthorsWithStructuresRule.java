package com.atmire.compliance.rules;

import com.atmire.compliance.definition.model.Value;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataValue;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.core.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 16/01/2018.
 */
public class CountAuthorsWithStructuresRule extends CountingComplianceRule {

    private static final Logger log = Logger.getLogger(CountAuthorsWithStructuresRule.class);

        public CountAuthorsWithStructuresRule(final String fieldDescription, final String fieldToCheck, final List<Value> thresholdValues) {
            super(fieldDescription, fieldToCheck, thresholdValues);
    }

    @Override
    protected boolean doValidationAndBuildDescription(Context context, Item item) {
        boolean valid = false;


        if (fieldToCheck == null) {
            addViolationDescription("cannot validate a blank field");
        } else {
            if (thresholdNumber != null) {

                MetadataField halMetadataField = null;
                try {
                    halMetadataField = ContentServiceFactory.getInstance().getMetadataFieldService().findByElement(context,"hal","structure","identifier");
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }

                for (MetadataValue metadataValue : item.getItemService().getMetadataByMetadataString(item, fieldToCheck)) {
                    List<MetadataValue> childValues = filterChildValues(metadataValue.getChildMetadataValues(),halMetadataField);
                    if (CollectionUtils.isNotEmpty(childValues) && CollectionUtils.size(childValues) > thresholdNumber) {
                        return true;
                    }
                }
                addViolationDescription("le nombre de %s est %d", fieldDescription, 0);
            }
        }
        return valid;
    }

    private List<MetadataValue> filterChildValues(List<MetadataValue> childValues, MetadataField fieldToKeep){
        if(CollectionUtils.isEmpty(childValues)){
            return childValues;
        }
        List<MetadataValue> valuesToKeep = new ArrayList<>();
        for(MetadataValue metadataValue : childValues){
            if(metadataValue.getMetadataField().equals(fieldToKeep)){
                valuesToKeep.add(metadataValue);
            }
        }
        return valuesToKeep;
    }
}
