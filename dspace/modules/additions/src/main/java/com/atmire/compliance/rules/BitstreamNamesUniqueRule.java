package com.atmire.compliance.rules;

import org.apache.log4j.Logger;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.BitstreamService;
import org.dspace.core.Context;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jonas - jonas@atmire.com on 03/05/2018.
 */
public class BitstreamNamesUniqueRule extends AbstractComplianceRule {

    /* Log4j logger*/
    private static final Logger log =  Logger.getLogger(BitstreamNamesUniqueRule.class);

    private String description;

    public BitstreamNamesUniqueRule(String description){
        this.description = description;
    }
    @Override
    protected String getRuleDescriptionCompliant() {
        return description;
    }

    @Override
    protected String getRuleDescriptionViolation() {
        return description;
    }

    @Override
    protected boolean doValidationAndBuildDescription(Context context, Item item) {
        BitstreamService bitstreamService = ContentServiceFactory.getInstance().getBitstreamService();
        Set<String> bitstreamNames = new HashSet<>();
        try {
            Iterator<Bitstream> itemBitstreams = bitstreamService.getItemBitstreams(context, item);
            while(itemBitstreams.hasNext()){
                Bitstream bitstream = itemBitstreams.next();
                if(bitstreamNames.contains(bitstream.getName())){
                    return false;
                }
                bitstreamNames.add(bitstream.getName());
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return true;
    }
}
