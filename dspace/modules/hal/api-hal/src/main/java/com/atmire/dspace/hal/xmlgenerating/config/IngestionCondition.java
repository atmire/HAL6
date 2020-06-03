package com.atmire.dspace.hal.xmlgenerating.config;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Created by jonas - jonas@atmire.com on 16/04/2018.
 */
public interface IngestionCondition {

    boolean shouldBeIngested(Element root) throws JDOMException;

}
