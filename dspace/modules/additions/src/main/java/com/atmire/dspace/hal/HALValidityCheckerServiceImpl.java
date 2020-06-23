package com.atmire.dspace.hal;

import com.atmire.dspace.hal.service.HALValidityCheckerService;
import com.atmire.dspace.hal.xmlgenerating.bitstream.BitstreamRetrievalService;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by jonas - jonas@atmire.com on 07/11/17.
 */
public class HALValidityCheckerServiceImpl implements HALValidityCheckerService {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(HALValidityCheckerServiceImpl.class);

    @Autowired
    BitstreamRetrievalService bitstreamRetrievalService;
    @Autowired
    BitstreamRetrievalService itemService;
    @Autowired
    AuthorizeService authorizeService;

    private boolean embargo2YearBlock = false;

    @Override
    public boolean isValidHalItem(Context context, Item item) throws SQLException, AuthorizeException {
        return embargoValid(context, item) && allBitstreamNamesUnique(item);
    }

    private boolean embargoValid(Context context, Item item) throws SQLException {
        List<Bundle> bundles = item.getItemService().getBundles(item,"ORIGINAL");
        for(Bundle bundle : bundles){
            for(Bitstream bitstream : bundle.getBitstreams()){
                if(bitstream!=null){
                    boolean valid = isBitstreamValid(context, bitstream);
                    if(!valid){
                        return valid;
                    }
                }
            }
        }
        return true;
    }

    private boolean allBitstreamNamesUnique(Item item) throws SQLException {
        Set<String> bitstreamNames = new HashSet<>();
        for (Bitstream bitstream : itemService.retrieveBitstreams(item)) {
            if(bitstreamNames.contains(bitstream.getName())){
                return false;
            }
            bitstreamNames.add(bitstream.getName());
        }
        return true;
    }

    protected boolean isBitstreamValid(Context context, Bitstream bitstream) throws SQLException {
        if(embargo2YearBlock)
        {
            // Check if the embargo currently on the file is longer than 2 years, if it isn't we don't add the file
            return checkEmbargoValidForExport(context, bitstream);
        } else {
            // Embargoed files are considered valid, we just don't export them later on
            return true;
        }
    }


    public boolean checkEmbargoValidForExport(Context context, Bitstream bitstream) throws SQLException {
        for (final ResourcePolicy readPolicy : authorizeService.getPoliciesActionFilter(context, bitstream, Constants.READ)) {
            boolean isAnonymousGroup = readPolicy.getGroup() != null && Group.ANONYMOUS.equals(readPolicy.getGroup().getName());
            boolean embargoSet = readPolicy.getStartDate() != null;
            if (isAnonymousGroup && !embargoSet) {
                return true;
            }
            if (isAnonymousGroup && embargoSet) {
                return dateIsValid(readPolicy);
            }
        }
        return false;
    }

    private boolean dateIsValid(ResourcePolicy readPolicy)  {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 2);
        Date dateToCheck = c.getTime();
        return readPolicy.getStartDate().before(dateToCheck);
    }

    public boolean isEmbargo2YearBlock() {
        return embargo2YearBlock;
    }

    @Required
    public void setEmbargo2YearBlock(boolean embargo2YearBlock) {
        this.embargo2YearBlock = embargo2YearBlock;
    }

}
