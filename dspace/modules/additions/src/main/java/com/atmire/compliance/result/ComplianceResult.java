package com.atmire.compliance.result;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class that represents the result of a compliance check
 */
public class ComplianceResult {

    private List<CategoryComplianceResult> categoryResults = new LinkedList<>();

    private List<RuleComplianceResult> exceptionResults = new LinkedList<>();

    private boolean isApplicable;

    private List<RuleComplianceResult> preconditionResults = new LinkedList<>();

    public List<CategoryComplianceResult> getOrderedCategoryResults() {
        Collections.sort(categoryResults);

        return categoryResults;
    }

    public List<RuleComplianceResult> getAppliedExceptions() {
        List<RuleComplianceResult> output = new LinkedList<>();
        for (RuleComplianceResult ruleResult : exceptionResults) {

            if(ruleResult.isCompliant() && ruleResult.isApplicable()) {
                output.add(ruleResult);
            }
        }

        return output;
    }

    public boolean isCompliant() {
        boolean isCompliant = true;

        if(!isCompliantByException()) {

            Iterator<CategoryComplianceResult> it = getOrderedCategoryResults().iterator();

            while (it.hasNext() && isCompliant) {
                CategoryComplianceResult next = it.next();
                isCompliant = next.isCompliant() || !next.isApplicable();
            }
        }

        return isCompliant;
    }

    public List<RuleComplianceResult> getViolatedPreconditions(){
        List<RuleComplianceResult> output = new LinkedList<RuleComplianceResult>();
        for (RuleComplianceResult preconditionResult : preconditionResults) {
            if(!preconditionResult.isCompliant()){
                output.add(preconditionResult);
            }
        }

        return output;
    }

    public boolean isCompliantByException() {
        return !getAppliedExceptions().isEmpty();
    }

    public void addExceptionResult(final RuleComplianceResult ruleResult) {
        exceptionResults.add(ruleResult);
    }

    public void addCategoryResult(final CategoryComplianceResult categoryResult) {
        categoryResults.add(categoryResult);
    }

    public void addPreconditionResult(final RuleComplianceResult ruleResult) {
        preconditionResults.add(ruleResult);
    }

    public boolean isApplicable() {
        return isApplicable;
    }

    public void setApplicable(boolean applicable) {
        isApplicable = applicable;
    }
}
