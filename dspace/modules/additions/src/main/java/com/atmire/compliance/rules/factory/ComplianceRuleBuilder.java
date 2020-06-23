package com.atmire.compliance.rules.factory;


import com.atmire.compliance.rules.AbstractComplianceRule;
import com.atmire.compliance.rules.ComplianceRule;
import com.atmire.compliance.definition.model.RuleDefinition;

/**
 * Interface for a builder class that is able to instantiate compliance vaidation rules
 */
public abstract class ComplianceRuleBuilder {

    public abstract ComplianceRule buildRule(final RuleDefinition ruleDefinition);

    protected void applyDefinitionDescriptionAndResolutionHint(final AbstractComplianceRule rule, final RuleDefinition ruleDefinition) {
        rule.setDefinitionHint(ruleDefinition.getDescription());
        rule.setResolutionHint(ruleDefinition.getResolutionHint());
    }
}
