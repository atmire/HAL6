package com.atmire.dspace.hal.factory;

import com.atmire.dspace.hal.service.HALDepositService;
import com.atmire.dspace.hal.service.HALRetrievalService;
import com.atmire.dspace.hal.service.HALStatusService;
import com.atmire.dspace.hal.service.HALValidityCheckerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jonas - jonas@atmire.com on 07/09/17.
 */
public class HALServiceFactoryImpl extends HALServiceFactory {

    @Autowired
    private HALDepositService halDepositService;

    @Autowired
    private HALRetrievalService halRetrievalService;

    @Autowired
    private HALValidityCheckerService halValidityCheckerService;

    @Autowired
    private HALStatusService halStatusService;

    @Override
    public HALDepositService getHALDepositService() {
        return halDepositService;
    }

    @Override
    public HALRetrievalService getHALRetrievalService() {
        return halRetrievalService;
    }

    @Override
    public HALValidityCheckerService getHalValidityCheckerService() {
        return halValidityCheckerService;
    }

    @Override
    public HALStatusService getHalStatusService() {
        return halStatusService;
    }
}
