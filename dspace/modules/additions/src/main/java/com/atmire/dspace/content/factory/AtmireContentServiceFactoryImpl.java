package com.atmire.dspace.content.factory;

import com.atmire.dspace.content.service.AtmireItemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jonas - jonas@atmire.com on 23/10/17.
 */
public class AtmireContentServiceFactoryImpl extends AtmireContentServiceFactory {

    @Autowired
    private AtmireItemService itemService;

    @Override
    public AtmireItemService getItemService() {
        return itemService;
    }
}
