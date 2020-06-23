package com.atmire.compliance.service;

import com.atmire.compliance.result.ComplianceResult;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Service to check if an item is compliant with the defined validation rules
 */
public interface ComplianceCheckService {

    ComplianceResult checkCompliance(final Context context, final Item item) throws SQLException;

    boolean blockOnWorkflow(String collectionHandle);
}
