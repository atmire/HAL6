package com.atmire.compliance.rules.factory;

import com.atmire.compliance.definition.model.RuleDefinition;
import com.atmire.compliance.rules.BitstreamNamesUniqueRule;
import com.atmire.compliance.rules.ComplianceRule;

/**
 * Created by jonas - jonas@atmire.com on 03/05/2018.
 */
public class BitstreamNamesUniqueRuleBuilder extends ComplianceRuleBuilder {
    @Override
    public ComplianceRule buildRule(RuleDefinition ruleDefinition) {
        BitstreamNamesUniqueRule rule = new BitstreamNamesUniqueRule(ruleDefinition.getFieldDescription());

        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }
}
