package com.atmire.compliance.rules.factory;

import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.rules.FieldHasValueRule;
import com.atmire.compliance.definition.model.RuleDefinition;

/**
 * Builder that will instantiate a ValueRule rule based on a rule definition.
 */
public class ValueRuleBuilder extends ComplianceRuleBuilder {

    public ComplianceRule buildRule(final RuleDefinition ruleDefinition) {
        FieldHasValueRule rule = new FieldHasValueRule(ruleDefinition.getFieldDescription(), ruleDefinition.getField().get(0),
                ruleDefinition.getFieldValue());
        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }

}
