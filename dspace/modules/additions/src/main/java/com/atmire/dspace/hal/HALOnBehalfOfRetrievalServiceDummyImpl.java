package com.atmire.dspace.hal;

import com.atmire.dspace.hal.service.HALOnBehalfOfRetrievalService;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jonas - jonas@atmire.com on 05/02/2018.
 */
public class HALOnBehalfOfRetrievalServiceDummyImpl implements HALOnBehalfOfRetrievalService {

    @Autowired
    private ConfigurationService configurationService;
    @Override
    public String retrieveOnBehalfOfHeader(Context context, Item item) {
        return configurationService.getProperty("hal.on-behalf-of");
    }
}
