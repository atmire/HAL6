package com.atmire.compliance.rules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.MetadataValue;
import com.atmire.compliance.definition.model.Value;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Rule that will check if an item field has a specified value
 */
public class FieldHasValueRule extends AbstractFieldCheckRule {

    private Map<String, Value> possibleValues = new LinkedHashMap<String, Value>();

    private Value checkedCompliantValue = null;

    public FieldHasValueRule(final String fieldDescription, final String metadataField, final Collection<Value> possibleValues) {
        super(fieldDescription, metadataField);

        if(CollectionUtils.isNotEmpty(possibleValues)) {
            for (Value possibleValue : possibleValues) {
                this.possibleValues.put(StringUtils.trimToEmpty(possibleValue.getValue()), possibleValue);
            }
        }
    }

    protected boolean checkFieldValues(final List<MetadataValue> fieldValueList) {

        if (isEmpty(fieldValueList)) {
            addViolationDescription("le %s field has no value", fieldDescription);
            return false;
        } else if(possibleValues.containsKey(fieldValueList.get(0).getValue())) {
            checkedCompliantValue = possibleValues.get(fieldValueList.get(0).getValue());
            return true;
        } else {
            addViolationDescription("le %s contient la valeur %s", fieldDescription, fieldValueList.get(0).getValue());
            return false;
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("le %s (%s) contient la valeur %s", fieldDescription, metadataFieldToCheck,
                checkedCompliantValue == null ? buildValueString() : getValueDescription(checkedCompliantValue));
    }

    protected String getRuleDescriptionViolation() {
        return String.format("le %s (%s) doit contient la valeur %s", fieldDescription, metadataFieldToCheck,
                checkedCompliantValue == null ? buildValueString() : getValueDescription(checkedCompliantValue));
    }

    private String buildValueString() {
        if(possibleValues.size() == 1) {
            return getValueDescription(possibleValues.values().iterator().next());

        } else if(possibleValues.size() > 1) {
            StringBuilder output = new StringBuilder();
            int i = 1;

            for (Value value : possibleValues.values()) {
                output.append(getValueDescription(value));
                if(i == (possibleValues.size() - 1)) {
                    output.append(" or ");
                } else if(i < possibleValues.size()){
                    output.append(", ");
                }
                i++;
            }

            return output.toString();
        } else {
            return "";
        }
    }
}
