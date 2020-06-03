package com.atmire.dspace.hal.xmlgenerating.filter;

import org.apache.log4j.Logger;
import org.dspace.content.Bundle;
import org.dspace.content.Item;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jonas on 23/06/15.
 */
public class DisabledBitstreamFilter implements DisabledElementFilter {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(DisabledBitstreamFilter.class);

    //check if an item has a bitstream (from the original bundle)
    // if a bitstream is found -> return false;
    // if no bitstream is found -> return true;
    @Override
    public boolean checkIfFilterElementIsAbsent(Item item) {
        try {

            List<Bundle> bundles = item.getItemService().getBundles(item,"ORIGINAL");
            for(Bundle b: bundles){
                // if size of bitstreams in original bundle; return false;
                if (b.getBitstreams().size()>0){
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            log.error(e, e);
        }
        return true;
    }
}
