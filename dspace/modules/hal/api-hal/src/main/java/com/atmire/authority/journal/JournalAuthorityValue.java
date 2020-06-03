package com.atmire.authority.journal;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;
import org.dspace.authority.AuthorityValue;

import java.util.Map;

/**
 * User: kevinvdv (kevin at atmire.com)
 * Date: 16/01/18
 * Time: 16:28
 */
public class JournalAuthorityValue extends AuthorityValue
{
    public static final String TYPE = "local-journal";
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getId() {
        final String nonDigestedIdentifier = JournalAuthorityValue.class.toString() + " field, " + getCategory() + ", title: " + title;
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        setTitle(value);
    }

    @Override
    public String getAuthorityType() {
        return TYPE;
    }

    @Override
    public AuthorityCategory getCategory() {
        return AuthorityCategory.JOURNAL;
    }

    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getTitle()).toString();
    }

    @Override
    public String toString() {
        return "JournalAuthorityValue{" +
                "title='" + title + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean hasTheSameInformationAs(Object o) {
        if (this == o) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JournalAuthorityValue that = (JournalAuthorityValue) o;
        return StringUtils.equalsIgnoreCase(that.getTitle(), getTitle());
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();
        if (StringUtils.isNotBlank(getTitle())) {
            doc.addField("label_title", getTitle());
        }
        return doc;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        if (StringUtils.isNotBlank(getTitle())) {
            map.put("title", getTitle());
        } else {
            map.put("title", "/");
        }

        return map;
    }
}
