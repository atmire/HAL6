package com.atmire.dspace.hal.factory;

import com.atmire.dspace.hal.service.HALDepositService;
import com.atmire.dspace.hal.service.HALRetrievalService;
import com.atmire.dspace.hal.service.HALStatusService;
import com.atmire.dspace.hal.service.HALValidityCheckerService;
import org.dspace.services.factory.DSpaceServicesFactory;

/**
 * Created by jonas - jonas@atmire.com on 07/09/17.
 */
public abstract class HALServiceFactory {

    public abstract HALDepositService getHALDepositService();

    public abstract HALRetrievalService getHALRetrievalService();

    public abstract HALValidityCheckerService getHalValidityCheckerService();

    public abstract HALStatusService getHalStatusService();

    public static HALServiceFactory getInstance(){
        return DSpaceServicesFactory.getInstance().getServiceManager().getServiceByName("halServiceFactory", HALServiceFactory.class);
    }
}
