package com.atmire.dspace.hal.xmlgenerating.attributes.metadata;

import com.atmire.dspace.hal.xmlgenerating.HALSwordXmlGeneratorImpl;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.DCDate;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jonas on 08/05/15.
 */
// Class intended to add the value of a given metadata-element as an attribute to an element
public class MetadataDateAsAttributeGenerator extends MetadataValueAsAttributeGenerator {


    @Override
    public void attachMetadataValue(Document doc, Element element, Item i, MetadataValue dcValue) {

        if(StringUtils.isNotBlank(dcValue.getValue())) {
            DCDate dcDate = new DCDate(dcValue.getValue());
            SimpleDateFormat dateIso = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dcDate.toDate();
            String dateFormatted= dateIso.format(d);
            element.setAttributeNS(HALSwordXmlGeneratorImpl.TEI_NAMESPACE,attributeName, dateFormatted);
            //element.setAttribute(attributeName, dateFormatted);
        }
    }


}

