package com.atmire.dspace.hal.utils;

import com.atmire.dspace.hal.Structure;
import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.service.HALRetrievalService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.content.MetadataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas - jonas@atmire.com on 02/10/17.
 */
public class HALConversionUtils {

    /* Log4j logger*/
    private static final Logger log = Logger.getLogger(HALConversionUtils.class);

    /**
     * Static method to convert a $$$-separated string, containing structureIds, into an Array of integers
     *
     * @param string $$$-separated String (e.g. 1234$$$567$$$8$$$90)
     * @return Id's, split as an Array of integers (e.g. {1234, 567, 8, 90})
     */
    public static List<String> splitStructureIdString(String string) {
        String[] splitIDs = StringUtils.split(string, "\\$\\$\\$");
        List<String> IDs = new ArrayList<>();
        for (String splitID : splitIDs) {
            if (StringUtils.isNotBlank(splitID)) {
                IDs.add(splitID);
            }
        }
        return IDs;
    }


    /**
     * The other way around, we call the method with an array of integers, and return these integers as a $$$-separated string
     *
     * @param structureIdInt an Array of Integers (eg. {123, 456, 7, 89, 0})
     * @return the structureIds in a $$$-separated String, to store them as metadata, or display them in one line
     */
    public static String convertStructureIdsIntToString(int[] structureIdInt) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < structureIdInt.length; i++) {
            output.append(structureIdInt[i]);
            if (i < structureIdInt.length - 1) output.append("$$$");
        }
        return output.toString();
    }

    /**
     * We split our string, and format it so it can be displayed (in HTML for example).
     * For every ID found, we do a SOAP lookup for the structure-name
     *
     * @return formatted text; id \t name \n
     */
    public static String convertStructureIdsStringToHTML(List<MetadataValue> list) {
       return convertStructureIdsStringToHTML(list,"\n", false);
    }
    /**
     * We split our string, and format it so it can be displayed (in HTML for example).
     * For every ID found, we do a SOAP lookup for the structure-name
     *
     * @return formatted text; id \t name \n
     */
    public static String convertStructureIdsStringToHTML(List<MetadataValue> list, String separator, boolean addId) {
        String html = "";
        HALRetrievalService halRetrievalService = HALServiceFactory.getInstance().getHALRetrievalService();
        try {
            for (int i = 0; i < list.size(); i++) {
                int id = Integer.parseInt(list.get(i).getValue());
                Structure structure = halRetrievalService.findStructureByID(id, false);
                if(structure == null) {
                    throw new Exception("Structure not found, id: " + id);
                }
                html += ((addId)?id + "\t":"") + structure.getName();
                if (i < list.size()- 1) html += separator;
            }
        } catch (Exception e) {
            log.error("Unprevented error", e);
            html = "status unknown";
        }
        return html;
    }

}
