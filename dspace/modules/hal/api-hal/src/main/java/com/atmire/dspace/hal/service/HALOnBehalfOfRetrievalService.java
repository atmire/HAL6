package com.atmire.dspace.hal.service;

import org.dspace.content.Item;
import org.dspace.core.Context;

/**
 * Created by jonas - jonas@atmire.com on 05/02/2018.
 */
public interface HALOnBehalfOfRetrievalService {

    String retrieveOnBehalfOfHeader(Context context, Item item);
}
