package com.atmire.dspace.hal.service;

import com.atmire.dspace.hal.Structure;

import java.util.List;
import java.util.Map;

/**
 * Created by jonas - jonas@atmire.com on 27/09/17.
 */
public interface HALRetrievalService {

    List<Structure> findStructuresByName(String name, Boolean allowInvalidStructures);

    List<Structure> findStructuresByAuthor(String lastName, String firstName, String email);

    String findFunction(String functionAbbreviation);

    Map<String, String> getMappedFunctionAbbreviations();

    Structure findStructureByID(int id);

    Structure findStructureByID(int id, boolean validated);
}
