package com.atmire.authority.domain;

import com.atmire.authority.hal.HALAuthorityValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;

import java.util.Map;

public class HalDomainAuthorityValue extends HALAuthorityValue {

    private String frDomain;
    private String code;
    private String level;
    private String parent;

    public String getId() {
        //We consider an ORCID value to be unique based on the identifier retrieved from ORCID.
        final String nonDigestedIdentifier = HalDomainAuthorityValue.class.toString() + " field, " + getCategory() + ", docid: " + getDocid();
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);    }

    public AuthorityCategory getCategory() {
        return AuthorityCategory.DOMAIN;
    }

    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getDocid()).toString();
    }


    public void setFrDomain(String frDomain) {
        this.frDomain = frDomain;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    public String getFrDomain() {
        return frDomain;
    }

    public String getCode() {
        return code;
    }

    public String getLevel() {
        return level;
    }

    public String getParent() {
        return parent;
    }

    public String getHalIdentifier(){
        return code;
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();

        doc.addField("label_fr_domain", getFrDomain());
        doc.addField("label_code", getCode());
        doc.addField("label_level", getLevel());
        doc.addField("label_parent", getParent());
        doc.addField("label_docid", getDocid());

        return doc;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        if (StringUtils.isNotBlank(getFrDomain())) {
            map.put("domain", getFrDomain());
        } else {
            map.put("domain", "/");
        }

        if (StringUtils.isNotBlank(getCode())) {
            map.put("code", getCode());
        } else {
            map.put("code", "/");
        }

        return map;
    }

    @Override
    public String getAuthorityType(){
        return "domain";
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
        final HalDomainAuthorityValue otherValue = (HalDomainAuthorityValue) object;
        return StringUtils.equals(this.getDocid(), otherValue.getDocid());
    }

    public int hashCode(){
        return 31 * getDocid().hashCode();
    }

    public boolean hasTheSameInformationAs(HalDomainAuthorityValue other){
        return (StringUtils.equals(this.getDocid(), other.getDocid()) &&
                StringUtils.equals(this.getFrDomain(), other.getFrDomain()) &&
                StringUtils.equals(this.getCode(), other.getCode()) &&
                StringUtils.equals(this.getLevel(), other.getLevel()) &&
                StringUtils.equals(this.getParent(), other.getParent())
        );
    }
}
