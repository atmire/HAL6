package com.atmire.compliance.rules.factory;

import com.atmire.compliance.definition.model.RuleDefinition;
import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.rules.FieldIsNotBlankRule;

/**
 * Created by jonas - jonas@atmire.com on 19/03/2018.
 */
public class NotBlankRuleBuilder extends ComplianceRuleBuilder {

    public ComplianceRule buildRule(final RuleDefinition ruleDefinition) {
        FieldIsNotBlankRule rule = new FieldIsNotBlankRule(ruleDefinition.getFieldDescription(), ruleDefinition.getField().get(0));
        applyDefinitionDescriptionAndResolutionHint(rule, ruleDefinition);
        return rule;
    }
}
