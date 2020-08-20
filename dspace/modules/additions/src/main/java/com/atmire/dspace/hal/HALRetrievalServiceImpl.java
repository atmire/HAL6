package com.atmire.dspace.hal;

import com.atmire.dspace.hal.service.HALRetrievalService;
import com.atmire.dspace.hal.v3.AuthorStructure;
import com.atmire.dspace.hal.v3.StructureStructure;
import com.atmire.dspace.hal.v3.UnvalidatedStructureStructure;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jonas - jonas@atmire.com on 27/09/17.
 */
public class HALRetrievalServiceImpl implements HALRetrievalService {

    @Autowired(required = true)
    protected StructureStructure structureStructure;

    @Autowired(required = true)
    protected AuthorStructure authorStructure;

    @Autowired(required = true)
    protected UnvalidatedStructureStructure unvalidatedStructureStructure;

    @Resource(name="mappedFunctionAbbreviations")
    Map<String, String> mappedFunctionAbbreviations;

    @Override
    public List<Structure> findStructuresByName(String name, Boolean allowInvalidStructures) {
        List<String> params = new ArrayList<String>();
        params.add("((name_t:*" + name + "*) OR (acronym_t:*" + name + "*))");
        if (allowInvalidStructures) {
            return unvalidatedStructureStructure.queryStructures(params, "docid");
        } else {
            return structureStructure.queryStructures(params, "docid");
        }

    }

    @Override
    public List<Structure> findStructuresByAuthor(String lastName, String firstName, String email) {
        lastName = lastName == null ? "" : lastName;
        firstName = firstName == null ? "" : firstName;
        email = email == null ? "" : email;

        Map<String, String> params = new HashMap<String, String>();
        params.put("firstName_t", firstName);
        params.put("lastName_t", lastName);
        if (StringUtils.isNotBlank(email)) {
            //Only add mail if we are searching for one.
            params.put("email_s", email);
        }
        /**
         * In order to ensure that we ONLY retrieve structures we need to filter them.
         * We can do this by retrieving them again by ID. This method has an extra filter query on type structure.
         */

        List<Structure> structures = authorStructure.queryStructures(params, null);
        Map<Integer, Structure> result = new TreeMap<Integer, Structure>();
        if(structures ==null) {
            structures = new ArrayList<>();
        }
        for (Structure structure : structures) {
            Structure structureByID = findStructureByID(structure.getId(), true);
            if (structureByID != null) {
                result.put(structureByID.getId(), structureByID);
            }
        }
        return new ArrayList<Structure>(result.values());
    }

    @Override
    public String findFunction(String functionAbbreviation) {
        if(StringUtils.isBlank(functionAbbreviation)){
            return null;
        }

        if(mappedFunctionAbbreviations!=null && mappedFunctionAbbreviations.containsKey(functionAbbreviation)){
            return mappedFunctionAbbreviations.get(functionAbbreviation);
        }
        return "Auteur";
    }

    public Map<String, String> getMappedFunctionAbbreviations(){
        return mappedFunctionAbbreviations;
    }

    @Override
    public Structure findStructureByID(int id) {
        return findStructureByID(id, false);
    }

    @Override
    public Structure findStructureByID(int id, boolean validated) {
        List<String> params = new ArrayList<String>();
        params.add("docid:" + String.valueOf(id));
        List<Structure> structures;
        if (validated) {
            structures = structureStructure.queryStructures(params, "docid");
        } else {
            structures = unvalidatedStructureStructure.queryStructures(params, "docid");
        }

        Structure structure = null;
        if (structures != null && !structures.isEmpty()) {
            structure = structures.get(0);
        }
        return structure;
    }
}
