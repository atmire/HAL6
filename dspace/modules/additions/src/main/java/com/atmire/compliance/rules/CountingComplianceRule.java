package com.atmire.compliance.rules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.core.Context;
import com.atmire.compliance.definition.model.Value;

import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 16/01/2018.
 */
public abstract class CountingComplianceRule extends AbstractComplianceRule {

    protected String fieldToCheck;
    protected String fieldDescription;

    protected Value thresholdValue;
    protected Integer thresholdNumber;

    public CountingComplianceRule(final String fieldDescription, final String fieldToCheck, final List<Value> thresholdValues) {
        this.fieldDescription = StringUtils.trimToEmpty(fieldDescription);
        this.fieldToCheck = StringUtils.trimToNull(fieldToCheck);

        thresholdValue = CollectionUtils.isEmpty(thresholdValues) ? null : thresholdValues.get(0);

        try {
            this.thresholdNumber = thresholdValue == null ? null : Integer.valueOf(StringUtils.trimToEmpty(thresholdValue.getValue()));
        } catch (NumberFormatException ex) {
            addViolationDescription("the provided threshold value %s is not a number", thresholdValue);
            this.thresholdNumber = null;
        }
    }
    protected String getRuleDescriptionCompliant() {
        return String.format("le nombre de %s (%s) est supérieur à %s", fieldDescription, fieldToCheck,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected String getRuleDescriptionViolation() {
        return String.format("le nombre de %s (field %s) doit être supérieur à %s", fieldDescription, fieldToCheck,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }


    @Override
    protected abstract boolean doValidationAndBuildDescription(Context context, Item item);
}
