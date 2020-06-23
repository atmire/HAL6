package com.atmire.dspace.hal.xmlgenerating.attributes.prechecked;

import org.dspace.content.Item;
import org.w3c.dom.Element;

/**
 * Created by jonas - jonas@atmire.com on 09/02/2018.
 */
public interface ConditionCheckingDataAttributGenerator {

    public void addAttribute(Item item, Element element);
}
