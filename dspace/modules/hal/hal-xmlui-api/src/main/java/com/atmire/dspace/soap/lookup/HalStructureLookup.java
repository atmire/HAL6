package com.atmire.dspace.soap.lookup;

import com.atmire.dspace.hal.Structure;
import com.atmire.dspace.hal.factory.HALServiceFactory;
import com.atmire.dspace.hal.service.HALRetrievalService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.reading.AbstractReader;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.dspace.core.LogManager;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 26/05/15
 * Time: 13:51
 */
public class HalStructureLookup extends AbstractReader implements Recyclable {

    private static Logger log = Logger.getLogger(HalStructureLookup.class);


    @Override
    public void generate() throws IOException, SAXException, ProcessingException {
        try {
            Request request = ObjectModelHelper.getRequest(objectModel);

            String action = request.getParameter("action");
            String invalidAllow = request.getParameter("invalidallowed");
            Boolean allowInvalidStructures = Boolean.parseBoolean(invalidAllow);
            JSONArray array = new JSONArray(); // We put all our results in an array of Json objects

            HALRetrievalService halRetrievalService = HALServiceFactory.getInstance().getHALRetrievalService();

            if (action.equalsIgnoreCase("getRefStructure_byAuthor")) {
                String firstName = request.getParameter("first");
                String lastName = request.getParameter("last");

                if ((firstName != null && firstName.length() > 0) && (lastName != null && lastName.length() > 0)) {
                    List<Structure> structures = halRetrievalService.findStructuresByAuthor(lastName, firstName, null);
                    if (structures != null) {
                        for (Structure structure : structures) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", structure.getId());
                            jsonObject.put("structureName", structure.getName());
                            array.add(jsonObject);
                        }
                    }
                }
            } else if (action.equalsIgnoreCase("getRefStructure_byName")) {
                String structureName = request.getParameter("name");

                if (structureName != null && structureName.length() > 0) {
                    List<Structure> structures = halRetrievalService.findStructuresByName(structureName, allowInvalidStructures);
                    if (structures != null) {
                        for (Structure structure : structures) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("id", structure.getId());
                            jsonObject.put("structureName", structure.getName());
                            array.add(jsonObject);
                        }
                    }
                }
            } else if (action.equalsIgnoreCase("getRefStructure_byId")) {
                int id = Integer.parseInt(request.getParameter("id"));

                if (id > 0) {
                    Structure structure = halRetrievalService.findStructureByID(id, true);
                    if (structure != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", id);
                        jsonObject.put("structureName", structure.getName());
                        array.add(jsonObject);
                    }
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Structures", array); // We wrap our array in one JSONObject to write to our response

            IOUtils.copy(new ByteArrayInputStream(jsonObject.toString().getBytes("UTF-8")), out);
            out.flush();
        } catch (MalformedURLException e) {
            log.error("MalFormed URL", e);
        }
    }
}
