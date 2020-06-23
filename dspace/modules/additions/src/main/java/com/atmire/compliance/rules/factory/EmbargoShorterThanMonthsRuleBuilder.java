package com.atmire.compliance.rules.factory;

import com.atmire.compliance.rules.EmbargoShorterThanMonthsRule;
import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.definition.model.RuleDefinition;

/**
 * Builder that will instantiate a DateRangeSmallerThan rule based on a rule definition.
 */
public class EmbargoShorterThanMonthsRuleBuilder extends ComplianceRuleBuilder {

    public ComplianceRule buildRule(final RuleDefinition ruleDefinition) {
        EmbargoShorterThanMonthsRule rule = new EmbargoShorterThanMonthsRule(ruleDefinition.getFieldDescription(), ruleDefinition.getFieldValue());
        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }

}
