package com.atmire.dspace.hal.xmlgenerating.bitstream;

import org.dspace.content.Bitstream;
import org.dspace.content.Item;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 04/01/2018.
 */
public interface BitstreamRetrievalService {

    List<Bitstream> retrieveBitstreams(Item item) throws SQLException;
}
