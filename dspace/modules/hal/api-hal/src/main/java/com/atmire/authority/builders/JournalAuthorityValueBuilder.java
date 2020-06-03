package com.atmire.authority.builders;

import com.atmire.authority.journal.JournalAuthorityValue;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.dspace.authority.factory.AuthorityValueBuilder;

import java.util.Collection;

/**
 * User: kevinvdv (kevin at atmire.com)
 * Date: 17/01/18
 * Time: 08:50
 */
public class JournalAuthorityValueBuilder extends AuthorityValueBuilder<JournalAuthorityValue> {
    @Override
    public JournalAuthorityValue buildAuthorityValue(String identifier, String content) {
        final JournalAuthorityValue authorityValue = buildAuthorityValue();
        authorityValue.setValue(content);
        return authorityValue;
    }

    /**
     * Build an authority value with the provided solr document. Only person authority specific fields are handled in this method,
     * the super method is used to set General fields.
     * @param document
     * The solr document of the authority value
     * @return
     * The created authority value
     */
    @Override
    public JournalAuthorityValue buildAuthorityValue(SolrDocument document)
    {
        JournalAuthorityValue authorityValue = super.buildAuthorityValue(document);
        authorityValue.setTitle(ObjectUtils.toString(document.getFirstValue("label_title")));
        return authorityValue;
    }


    @Override
    public JournalAuthorityValue buildAuthorityValue() {
        return new JournalAuthorityValue();
    }

    @Override
    public boolean supports(String authorityValueType) {
        return StringUtils.equalsIgnoreCase(new JournalAuthorityValue().getAuthorityType(), authorityValueType);
    }
}
