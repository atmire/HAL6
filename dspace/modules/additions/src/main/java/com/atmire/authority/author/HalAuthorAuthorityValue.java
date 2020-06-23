package com.atmire.authority.author;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;
import org.dspace.authority.PersonAuthorityValue;

import java.util.Map;

public class HalAuthorAuthorityValue extends PersonAuthorityValue {

    public static final String TYPE = "hal_author";

    private String docid;
    private String idHal;
    private String idHalString;

    public String getIdHal() {
        return idHal;
    }

    public String getIdHalString() {
        return idHalString;
    }

    public void setIdHal(String idHal) {
        this.idHal = idHal;
    }

    public void setIdHalString(String idHalString) {
        this.idHalString= idHalString;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }



    public String getId() {
        //We consider an ORCID value to be unique based on the identifier retrieved from ORCID.
        final String nonDigestedIdentifier = HalAuthorAuthorityValue.class.toString() + " field, " + getCategory() + ", docid: " + getDocid();
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);
    }
    public AuthorityCategory getCategory() {
        return AuthorityCategory.PERSON;
    }

    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getDocid()).toString();
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();

        doc.addField("label_docid", getDocid());
        doc.addField("label_idhal", getIdHal());
        doc.addField("label_idhalString", getIdHalString());

        return doc;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        if (StringUtils.isNotBlank(getIdHal())) {
            map.put("Hal-ID", getIdHal());
        } else {
            map.put("Hal-ID", "/");
        }
        if(StringUtils.isNotBlank(getIdHalString())){
            map.put("HAL-name",getIdHalString());
        }

        return map;
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
        final HalAuthorAuthorityValue otherValue = (HalAuthorAuthorityValue) object;
        return StringUtils.equals(this.getDocid(), otherValue.getDocid());
    }
    public int hashCode(){
        return 31 * getDocid().hashCode();
    }
    public boolean hasTheSameInformationAs(HalAuthorAuthorityValue other){
        return (StringUtils.equals(this.getIdHal(), other.getIdHal()) &&
                StringUtils.equals(this.getIdHalString(), other.getIdHalString()) &&
                StringUtils.equals(this.getDocid(), other.getDocid()) &&
                StringUtils.equals(this.getLastName(), other.getLastName()) &&
                StringUtils.equals(this.getFirstName(), other.getFirstName())
        );

    }
    @Override
    public String getAuthorityType(){
        return TYPE;
    }



}
