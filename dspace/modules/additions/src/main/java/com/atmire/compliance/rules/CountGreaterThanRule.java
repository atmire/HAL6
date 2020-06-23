package com.atmire.compliance.rules;


import org.apache.commons.collections.CollectionUtils;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import com.atmire.compliance.definition.model.Value;

import java.sql.SQLException;
import java.util.List;

/**
 * Rule to check if an item has more than X values for a specified field.
 */
public class CountGreaterThanRule extends CountingComplianceRule {

    public CountGreaterThanRule(final String fieldDescription, final String fieldToCheck, final List<Value> thresholdValues) {
       super(fieldDescription, fieldToCheck, thresholdValues);
    }

    protected boolean doValidationAndBuildDescription(final Context context, final Item item) {
        boolean valid = false;

        if (fieldToCheck == null) {
            addViolationDescription("cannot validate a blank field");
        } else {
            if(thresholdNumber != null) {
                try {
                    int count = countFieldValues(context, item);

                    if (count > thresholdNumber) {
                        valid = true;
                    } else {
                        addViolationDescription("le nombre de %s est %d", fieldDescription, count);
                    }

                } catch (SQLException e) {
                    addViolationDescription("unable to count values for field %s: %s", fieldDescription, e.getMessage());
                }
            }
        }
        return valid;
    }

    private int countFieldValues(final Context context, final Item item) throws SQLException {
        List<MetadataValue> fieldValueList = getMetadata(context, item, fieldToCheck);
        return CollectionUtils.size(fieldValueList);
    }

}
