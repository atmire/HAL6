package com.atmire.compliance.rules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.authorize.factory.AuthorizeServiceFactory;
import org.dspace.authorize.service.ResourcePolicyService;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import com.atmire.compliance.definition.model.Value;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Rule to check if a date range defined by two metadata fields is smaller than a specified threshold
 */
public class EmbargoShorterThanMonthsRule extends AbstractComplianceRule {

    private String description;

    private Value thresholdValue;
    private Integer thresholdNumber;

    public EmbargoShorterThanMonthsRule(String description, List<Value> fieldValue) {
        this.description = StringUtils.trimToNull(description);
        thresholdValue = CollectionUtils.isEmpty(fieldValue) ? null : fieldValue.get(0);

        try {
            this.thresholdNumber = thresholdValue == null ? null : Integer.valueOf(StringUtils.trimToEmpty(thresholdValue.getValue()));
        } catch (NumberFormatException ex) {
            addViolationDescription("the provided threshold value %s is not a number", thresholdValue);
            this.thresholdNumber = null;
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("L'%s sur les fichiers liés ne peut excéder %s mois", description, thresholdNumber,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected String getRuleDescriptionViolation() {
        return String.format("L'%s sur les fichiers liés ne peut excéder %s mois", description, thresholdNumber,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected boolean doValidationAndBuildDescription(final Context context, final Item item) {
        ResourcePolicyService resourcePolicyService = AuthorizeServiceFactory.getInstance().getResourcePolicyService();
        try {
            List<Bundle> bundles = item.getItemService().getBundles(item, "ORIGINAL");
            for (Bundle bundle : bundles) {
                for (Bitstream bitstream : bundle.getBitstreams()) {
                    List<ResourcePolicy> resourcePolicies = resourcePolicyService.find(context, bitstream, Constants.READ);
                    for (ResourcePolicy readPolicy : resourcePolicies) {
                        boolean isAnonymousGroup = readPolicy.getGroup() != null && Group.ANONYMOUS.equals(readPolicy.getGroup().getName());
                        boolean embargoSet = readPolicy.getStartDate() != null;
                        if (isAnonymousGroup && embargoSet) {
                            boolean dateValid = dateIsValid(readPolicy);
                            if (!dateValid) {
                                return dateValid;
                            }
                        }
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    private boolean dateIsValid(ResourcePolicy readPolicy) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, thresholdNumber);
        Date dateToCheck = c.getTime();
        return readPolicy.getStartDate().before(dateToCheck);
    }


}
