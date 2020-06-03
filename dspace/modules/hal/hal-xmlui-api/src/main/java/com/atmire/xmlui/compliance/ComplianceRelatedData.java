package com.atmire.xmlui.compliance;

import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Division;
import com.atmire.compliance.result.ComplianceResult;
import org.dspace.core.Context;

/**
 * @author philip at atmire.com
 */
public interface ComplianceRelatedData {

    public void renderRelatedData(Context context, org.dspace.content.Item item, ComplianceResult result, Division div) throws WingException;
}
