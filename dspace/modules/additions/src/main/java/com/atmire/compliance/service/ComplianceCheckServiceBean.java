package com.atmire.compliance.service;

import com.atmire.compliance.result.ComplianceResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.atmire.compliance.rules.CompliancePolicy;
import com.atmire.compliance.rules.exception.ValidationRuleDefinitionException;
import com.atmire.compliance.rules.factory.ComplianceCategoryRulesFactory;
import org.dspace.content.Item;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * Implementation of {@link ComplianceCheckService}
 */
public class ComplianceCheckServiceBean implements ComplianceCheckService {

    private static Logger log = Logger.getLogger(ComplianceCheckServiceBean.class);

    private final String blockWorkflowConfig = ".workflow.block.on.rule.violation.";
    private final String defaultConfig = "default";

    private String identifier;

    private ComplianceCategoryRulesFactory rulesFactory;

    @Autowired
    private ItemService itemService;

    public ComplianceResult checkCompliance(final Context context, final Item item) throws SQLException {
        CompliancePolicy policy = getCompliancePolicy();

        if (policy == null) {
            return new ComplianceResult();

        } else {
            ComplianceResult complianceResult = null;

            try {
                complianceResult = policy.validatePreconditionRules(context, item);

                if (complianceResult.isApplicable()) {
                    complianceResult = policy.validate(context, item, complianceResult);

                }

            } catch(Exception ex) {
                log.warn(ex.getMessage(), ex);

            }

            return complianceResult;
        }
    }

    @Override
    public boolean blockOnWorkflow(String collectionHandle) {
        String blockWorkflowOnViolation = org.dspace.core.ConfigurationManager.getProperty("item-compliance",
                identifier + blockWorkflowConfig + collectionHandle);

        if (StringUtils.isBlank(blockWorkflowOnViolation)) {
            blockWorkflowOnViolation = org.dspace.core.ConfigurationManager.getProperty("item-compliance",
                    identifier + blockWorkflowConfig + defaultConfig);
        }

        if (StringUtils.isNotBlank(blockWorkflowOnViolation)) {
            return Boolean.parseBoolean(blockWorkflowOnViolation);
        }

        return false;
    }

    private CompliancePolicy getCompliancePolicy() {
        try {
            return rulesFactory.createComplianceRulePolicy();
        } catch (ValidationRuleDefinitionException e) {
            log.warn("Unable to load the validation rules: " + e.getMessage(), e);
        }
        return null;
    }

    public void setRulesFactory(ComplianceCategoryRulesFactory rulesFactory) {
        this.rulesFactory = rulesFactory;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
