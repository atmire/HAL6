package com.atmire.compliance.rules;

import org.dspace.content.MetadataValue;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 19/03/2018.
 */
public class FieldIsNotBlankRule extends AbstractFieldCheckRule {

    public FieldIsNotBlankRule(final String fieldDescription, final String metadataField) {
        super(fieldDescription, metadataField);
    }

    @Override
    protected boolean checkFieldValues(final List<MetadataValue> fieldValueList) {
        if (CollectionUtils.isEmpty(fieldValueList)) {
            addViolationDescription("le champ %s est vide", fieldDescription);
            return false;
        } else {
            return true;
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("le champ %s (%s) est rempli", fieldDescription, metadataFieldToCheck);
    }

    protected String getRuleDescriptionViolation() {
        return String.format("le champ %s (%s) doit être rempli", fieldDescription, metadataFieldToCheck);
    }
}