package com.atmire.xmlui.aspect.hal;

import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.service.HALDepositService;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.http.HttpEnvironment;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.app.xmlui.utils.ContextUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.factory.AuthorizeServiceFactory;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Item;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.handle.factory.HandleServiceFactory;
import org.dspace.handle.service.HandleService;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jonas - jonas@atmire.com on 08/11/17.
 */
public class ResendToHALAction extends AbstractAction implements ThreadSafe {

     /* Log4j logger*/
    private static final Logger log = Logger.getLogger(ResendToHALAction.class);

    private static final String ITEM_ID_PARAM = "itemID";
    private ItemService itemService = ContentServiceFactory.getInstance().getItemService();
    private AuthorizeService authorizeService = AuthorizeServiceFactory.getInstance().getAuthorizeService();
    private HandleService handleService = HandleServiceFactory.getInstance().getHandleService();
    private HALDepositService halDepositService = HALServiceFactory.getInstance().getHALDepositService();


    @Override
    public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {

        Context context = ContextUtil.obtainContext(objectModel);
        if(!authorizeService.isAdmin(context)){
            return null;
        }
        String itemIDFromParams = parameters.getParameter(ITEM_ID_PARAM);
        if (StringUtils.isBlank(itemIDFromParams)) {
            return null;
        }
        Item item = itemService.find(context, UUID.fromString(itemIDFromParams));
        if (item != null) {
            try {
                boolean metadataOnly = parameters.isParameter("metadataOnly");
                halDepositService.depositSubmissionToHal(context, item, metadataOnly);
            } catch (Exception e) {
                log.error(LogManager.getHeader(context, "resent_to_hal_error", "Item: " + itemIDFromParams), e);
            }

            HttpServletResponse httpResponse = (HttpServletResponse) objectModel.get(HttpEnvironment.HTTP_RESPONSE_OBJECT);
            httpResponse.sendRedirect(handleService.resolveToURL(context, item.getHandle()));
        }
        return null;
    }
}
