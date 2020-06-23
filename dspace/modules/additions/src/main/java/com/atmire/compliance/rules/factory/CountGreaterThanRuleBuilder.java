package com.atmire.compliance.rules.factory;

import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.rules.CountGreaterThanRule;
import com.atmire.compliance.definition.model.RuleDefinition;

/**
 * Builder that will instantiate a CountGreaterThan rule based on a rule definition.
 */
public class CountGreaterThanRuleBuilder extends ComplianceRuleBuilder {

    public ComplianceRule buildRule(final RuleDefinition ruleDefinition) {
        CountGreaterThanRule rule = new CountGreaterThanRule(ruleDefinition.getFieldDescription(), ruleDefinition.getField().get(0), ruleDefinition.getFieldValue());
        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }

}
