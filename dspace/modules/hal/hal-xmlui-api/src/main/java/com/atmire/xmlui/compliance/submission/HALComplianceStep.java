package com.atmire.xmlui.compliance.submission;

import org.apache.cocoon.ProcessingException;
import com.atmire.xmlui.compliance.ComplianceUI;
import org.dspace.app.xmlui.aspect.submission.AbstractSubmissionStep;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.utils.DSpace;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 15/01/2018.
 */
public class HALComplianceStep  extends AbstractSubmissionStep {

    protected static final Message T_error_blocked =
            message("xmlui.compliance.submission.error_blocked");

    protected static final Message T_head =
            message("xmlui.compliance.submission.head");

    private ComplianceUI complianceUI =  new DSpace().getServiceManager().getServiceByName("halComplianceUI", ComplianceUI.class);

    @Override
    public List addReviewSection(List reviewList) throws SAXException, WingException, UIException, SQLException, IOException, AuthorizeException {
        return null;
    }

    @Override
    public void addBody(Body body) throws SAXException, WingException, UIException, SQLException, IOException, AuthorizeException, ProcessingException {
        org.dspace.content.Item item = submission.getItem();
        Collection collection = submission.getCollection();
        String actionURL = contextPath + "/handle/"+collection.getHandle() + "/submit/" + knot.getId() + ".continue";

        Division div = body.addInteractiveDivision("submit-describe",actionURL,Division.METHOD_POST,"primary submission");
        div.setHead(T_submission_head);
        addSubmissionProgressList(div);

         div.addList("submit-compliance-1",List.TYPE_FORM).setHead(T_head.parameterize(complianceUI.getShortname()));

        complianceUI.addComplianceSections(div, item, context);

        if (this.errorFlag== com.atmire.compliance.hal.submission.HALComplianceStep.STATUS_BLOCKED)
        {
            div.addPara("compliance-error", "compliance-error").addContent(T_error_blocked);
        }

        List form = div.addList("submit-compliance-2",List.TYPE_FORM);

        addControlButtons(form);
    }
}
