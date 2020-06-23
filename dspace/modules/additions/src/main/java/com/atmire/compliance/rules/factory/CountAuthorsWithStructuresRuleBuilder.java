package com.atmire.compliance.rules.factory;

import com.atmire.compliance.rules.CountAuthorsWithStructuresRule;
import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.definition.model.RuleDefinition;

/**
 * Created by jonas - jonas@atmire.com on 16/01/2018.
 */
public class CountAuthorsWithStructuresRuleBuilder extends ComplianceRuleBuilder {
    @Override
    public ComplianceRule buildRule(RuleDefinition ruleDefinition) {
        CountAuthorsWithStructuresRule rule = new CountAuthorsWithStructuresRule(ruleDefinition.getFieldDescription(), ruleDefinition.getField().get(0), ruleDefinition.getFieldValue());

        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }
}
