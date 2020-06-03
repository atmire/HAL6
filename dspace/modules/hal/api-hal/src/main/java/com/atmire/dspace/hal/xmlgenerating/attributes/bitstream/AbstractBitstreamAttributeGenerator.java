package com.atmire.dspace.hal.xmlgenerating.attributes.bitstream;

import org.springframework.beans.factory.annotation.Required;

/**
 * Created by jonas - jonas@atmire.com on 05/01/2018.
 */
public abstract class AbstractBitstreamAttributeGenerator  implements BitstreamDependentAttributeGenerator{

    protected String attributeName;

    @Required
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


}
