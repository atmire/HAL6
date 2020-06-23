package com.atmire.authority.journal;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;
import org.dspace.authority.AuthorityValue;

import java.util.Map;

public class HalJournalAuthorityValue extends AuthorityValue {

    private String title;
    private String issn;
    private String publisher;
    private String docid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }



    public String getId() {
        //We consider an ORCID value to be unique based on the identifier retrieved from ORCID.
        final String nonDigestedIdentifier = HalJournalAuthorityValue.class.toString() + " field, " + getCategory() + ", docid: " + getDocid();
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();

        doc.addField("label_title", getTitle());
        doc.addField("label_issn", getIssn());
        doc.addField("label_publisher", getPublisher());
        doc.addField("label_docid", getDocid());

        return doc;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        if (StringUtils.isNotBlank(getIssn())) {
            map.put("issn", getIssn());
        } else {
            map.put("issn", "/");
        }

        if (StringUtils.isNotBlank(getPublisher())) {
            map.put("publisher", getPublisher());
        } else {
            map.put("publisher", "/");
        }

        if (StringUtils.isNotBlank(getTitle())) {
            map.put("title", getTitle());
        } else {
            map.put("title", "/");
        }
        return map;
    }


    @Override
    public String getAuthorityType(){
        return "journal";
    }

    public AuthorityCategory getCategory() {
        return AuthorityCategory.JOURNAL;
    }
    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getDocid()).toString();
    }

    @Override
    public boolean equals(Object object){
        if (object == null)
        {
            return false;
        }
        if(object.getClass() != this.getClass())
        {
            return false;
        }
        final HalJournalAuthorityValue otherValue = (HalJournalAuthorityValue) object;
        return StringUtils.equals(this.getDocid(), otherValue.getDocid());
    }

    public int hashCode(){
        return 31 * getDocid().hashCode();
    }

    public boolean hasTheSameInformationAs(HalJournalAuthorityValue other){
        return (StringUtils.equals(this.getDocid(), other.getDocid()) &&
                StringUtils.equals(this.getIssn(), other.getIssn()) &&
                StringUtils.equals(this.getPublisher(), other.getPublisher()) &&
                StringUtils.equals(this.getTitle(), other.getTitle())
                );
    }
}
