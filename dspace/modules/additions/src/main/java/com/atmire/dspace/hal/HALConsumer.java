package com.atmire.dspace.hal;

import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.service.HALDepositService;
import org.apache.log4j.Logger;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.ItemService;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.event.Consumer;
import org.dspace.event.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jonas - jonas@atmire.com on 07/09/17.
 */
public class HALConsumer implements Consumer {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(HALConsumer.class);

    private Set<UUID> itemsToUpdate;
    private ItemService itemService = ContentServiceFactory.getInstance().getItemService();
    private HALDepositService halDepositService = HALServiceFactory.getInstance().getHALDepositService();

    @Override
    public void initialize() throws Exception {

    }

    @Override
    public void consume(Context context, Event event) throws Exception {
        if(itemsToUpdate==null){
            itemsToUpdate = new HashSet<>();
        }
        DSpaceObject dso = event.getSubject(context);
        if(dso.getType() == Constants.ITEM && event.getEventType() == Event.INSTALL)
        {
            itemsToUpdate.add(dso.getID());
        }
    }

    @Override
    public void end(Context context) throws Exception {
        context.turnOffAuthorisationSystem();
        try{
            if (itemsToUpdate != null) {
                for (UUID id : itemsToUpdate) {
                    try {
                        Item item = itemService.find(context, id);
                        halDepositService.depositSubmissionToHal(context, item, false);
                    } catch (Exception e) {
                        log.error(LogManager.getHeader(context, "hal_consumer_error", "Item: " + id), e);
                    }
                }
            }
        } finally {
            context.restoreAuthSystemState();
            itemsToUpdate = null;
        }
    }

    @Override
    public void finish(Context context) throws Exception {

    }
}
