package com.atmire.compliance.rules;

import com.atmire.compliance.result.RuleComplianceResult;
import org.dspace.content.Item;
import org.dspace.core.Context;

/**
 * Contract of a compliance rule. A compliance rule should be able to validate an item. But a compliance rule
 * can also have preconditions (before the rule is applicable) and exceptions.
 */
public interface ComplianceRule {

    RuleComplianceResult validate(Context context, Item item);

    void addExceptionRule(ComplianceRule exceptionRule);

    void addPreconditionRule(ComplianceRule complianceRule);
}
