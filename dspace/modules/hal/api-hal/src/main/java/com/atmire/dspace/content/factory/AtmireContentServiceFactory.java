package com.atmire.dspace.content.factory;

import com.atmire.dspace.content.service.AtmireItemService;
import org.dspace.services.factory.DSpaceServicesFactory;

/**
 * Created by jonas - jonas@atmire.com on 23/10/17.
 */
public abstract class AtmireContentServiceFactory {

    public abstract AtmireItemService getItemService();

    public static AtmireContentServiceFactory getInstance(){
        return DSpaceServicesFactory.getInstance().getServiceManager().getServiceByName("atmireContentServiceFactory", AtmireContentServiceFactory.class);
    }

}
