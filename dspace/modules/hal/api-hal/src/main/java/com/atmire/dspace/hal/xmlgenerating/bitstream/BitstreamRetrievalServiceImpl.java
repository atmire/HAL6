package com.atmire.dspace.hal.xmlgenerating.bitstream;

import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.content.service.BitstreamService;
import org.dspace.content.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public class BitstreamRetrievalServiceImpl implements BitstreamRetrievalService {
    @Autowired
    BitstreamService bitstreamService;
    @Autowired
    ItemService itemService;

    @Override
    public List<Bitstream> retrieveBitstreams(Item item) throws SQLException {
        List<Bundle> bundles = itemService.getBundles(item, "ORIGINAL");
        List<Bitstream> bitstreams = new ArrayList<>();
        for(Bundle bundle : bundles){
            bitstreams.addAll(bundle.getBitstreams());
        }
        return bitstreams;
    }

}
