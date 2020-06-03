package com.atmire.compliance.rules;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dspace.authorize.factory.AuthorizeServiceFactory;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.eperson.factory.EPersonServiceFactory;
import org.dspace.eperson.service.GroupService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public enum CustomField {

    BITSTREAM_COUNT("bitstream.count") {
        public List<MetadataValue> createValueList(final Context context, final Item item) throws SQLException {
            List<MetadataValue> output = new LinkedList<MetadataValue>();
            AuthorizeService authorizeService = AuthorizeServiceFactory.getInstance().getAuthorizeService();
            GroupService groupService = EPersonServiceFactory.getInstance().getGroupService();
            UUID anonymousID = groupService.findByName(context, Group.ANONYMOUS).getID();
            List<Bitstream> bitstreams = new ArrayList<>();
            for (Bundle bundle : item.getItemService().getBundles(item, "ORIGINAL")) {
                bitstreams.addAll(bundle.getBitstreams());
            }
            for (Bitstream bitstream : CollectionUtils.emptyIfNull(bitstreams)) {
                isValid(bitstream, output);
            }

            return output;
        }

        // Currently all files are valid. Method that allows custom checks per bitstream
        private void isValid(Bitstream bitstream, List<MetadataValue> output) {
            output.add(null);

        }
    };

    private final String fieldName;

    CustomField(final String field) {
        this.fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public abstract List<MetadataValue> createValueList(final Context context, final Item item) throws SQLException;


    public static CustomField findByField(final String field) {
        CustomField result = null;

        for (CustomField customField : CustomField.values()) {
            if (StringUtils.equals(customField.getFieldName(), field)) {
                result = customField;
                break;
            }
        }

        return result;
    }

}
