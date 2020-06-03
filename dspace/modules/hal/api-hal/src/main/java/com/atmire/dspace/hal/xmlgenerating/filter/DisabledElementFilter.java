package com.atmire.dspace.hal.xmlgenerating.filter;

import org.dspace.content.Item;

/**
 * Created by jonas on 23/06/15.
 */
public interface DisabledElementFilter {

    public abstract boolean checkIfFilterElementIsAbsent(Item i);

}
