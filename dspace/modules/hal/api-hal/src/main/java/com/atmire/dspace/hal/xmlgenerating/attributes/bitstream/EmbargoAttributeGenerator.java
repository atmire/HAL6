package com.atmire.dspace.hal.xmlgenerating.attributes.bitstream;

import org.apache.commons.lang.time.DateFormatUtils;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Bitstream;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import java.sql.SQLException;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public class EmbargoAttributeGenerator extends AbstractBitstreamAttributeGenerator implements BitstreamDependentAttributeGenerator {

    private static final String DATEFORMAT = "yyyy-MM-dd";
    @Autowired
    AuthorizeService authorizeService;

    @Override
    public void addAttribute(Bitstream bitstream, Element element) throws SQLException {
        if(bitstream!=null){
            Context context = new Context();
            for (final ResourcePolicy readPolicy : authorizeService.getPoliciesActionFilter(context, bitstream, Constants.READ)) {
                if (readPolicy.getGroup() != null && Group.ANONYMOUS.equals(readPolicy.getGroup().getName()) && readPolicy.getStartDate() != null) {
                    final String dateString = DateFormatUtils.format(readPolicy.getStartDate(), DATEFORMAT);
                    element.setAttribute(attributeName, dateString);
                }
            }
        }
    }
}
