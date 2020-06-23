package com.atmire.xmlui.aspect.hal;

import com.atmire.compliance.result.ComplianceResult;
import com.atmire.compliance.service.ComplianceCheckService;
import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.factory.HALServiceFactoryImpl;
import com.atmire.dspace.hal.service.HALStatusService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.HandleUtil;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Options;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.factory.AuthorizeServiceFactory;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.service.ItemService;
import org.dspace.utils.DSpace;
import org.dspace.versioning.VersionHistory;
import org.dspace.versioning.factory.VersionServiceFactory;
import org.dspace.versioning.service.VersionHistoryService;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 07/11/17.
 */
public class Navigation extends AbstractDSpaceTransformer {
    /* Log4j logger*/
    private static final Logger log = Logger.getLogger(Navigation.class);

    private static final Message T_head = message("xmlui.hal.Navigation.head");
    private static final Message T_compliance_compliant = message("xmlui.hal.Navigation.compliance.compliant");
    private static final Message T_compliance_not_compliant = message("xmlui.hal.Navigation.compliance.not-compliant");

    private static final String FALLBACK_STATUS = "xmlui.hal.status.unknown";
    private static final String ERROR_STATUS = "xmlui.hal.status.error";

    protected AuthorizeService authorizeService = AuthorizeServiceFactory.getInstance().getAuthorizeService();
    protected HALStatusService halStatusService = HALServiceFactory.getInstance().getHalStatusService();
    protected VersionHistoryService versionHistoryService = VersionServiceFactory.getInstance().getVersionHistoryService();
    private ComplianceCheckService complianceCheckService = new DSpace().getServiceManager().getServiceByName("halComplianceCheckService", ComplianceCheckService.class);

    public void addPageMeta(PageMeta pageMeta) throws SAXException,
            WingException, UIException, SQLException, IOException,
            AuthorizeException {
        super.addPageMeta(pageMeta);
        DSpaceObject dso = HandleUtil.obtainHandle(objectModel);
        if (dso instanceof Item) {
            pageMeta.addMetadata("itemID").addContent(dso.getID().toString());
        }
    }

    public void addOptions(Options options) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException {
        options.addList("browse");
        options.addList("account");
        options.addList("context");
        if (authorizeService.isAdmin(context)) {
            // Context Administrative options
            DSpaceObject dso = HandleUtil.obtainHandle(objectModel);
            if (dso instanceof Item ) {
                Item item = (Item) dso;
                VersionHistory history = versionHistoryService.findByItem(context, item);
                if(history!=null){
                    if(!versionHistoryService.getLatestVersion(context, history).getItem().equals(item)){
                        return;
                    }
                }
                String id = String.valueOf(dso.getID());
                List hal = options.addList("hal");
                hal.setHead(T_head);
                hal.addItem("hal-status", "hal-status").addXref(contextPath + "/hal/status/" + id, message("xmlui.hal.Navigation.hal-status"));
                org.dspace.app.xmlui.wing.element.Item status = hal.addItem("halStatus", "hidden");
                if (HALServiceFactoryImpl.getInstance().getHalValidityCheckerService().isValidHalItem(context, item)) {
                    status.addContent("halValid");
                    hal.addItem("itemID", "hidden").addContent(id);
                }

                ItemService itemService = item.getItemService();
                String identifier = itemService.getMetadata(item, "hal.identifier");
                String version = itemService.getMetadata(item, "hal.version");
                String error
                        = itemService.getMetadata(item, "hal.description.error");
                if (StringUtils.isNotBlank(identifier)) {
                    hal.addItem("halIdentifier", "hidden").addContent(identifier);
                    if (StringUtils.isNotBlank(version)) {
                        hal.addItem("halVersion", "hidden").addContent(version);
                    }
                    String value = halStatusService.retrieveHALStatus(identifier, version);
                    if (StringUtils.isNotBlank(value)) {
                        if (StringUtils.equals(value, "error")) {
                            value = ERROR_STATUS;
                        }
                        hal.addItem("retrievedStatus", "hidden").addContent(value);

                    } else {
                        hal.addItem("retrievedStatus", "hidden").addContent(FALLBACK_STATUS);
                    }
                }
                ComplianceResult complianceResult = complianceCheckService.checkCompliance(context, item);
                if(complianceResult!=null){
                    hal.addItem("halCompliance", "hidden").addContent((complianceResult.isCompliant())?T_compliance_compliant:T_compliance_not_compliant);
                    hal.addItem("halComplianceLink", "hidden").addContent(contextPath + "/handle/" + dso.getHandle() + "/hal-compliance");
                }
                if (StringUtils.isNotBlank(error)) {
                    hal.addItem("halError", "hidden").addContent(error);
                }
            }
        }

        options.addList("administrative");
    }

}
