package com.atmire.authority.project;

import com.atmire.authority.hal.HALAuthorityValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;

import java.util.Map;

public class HalAnrProjectAuthorityValue extends HALAuthorityValue {
    private String title;
    private String reference;
    private String valid;
    private String acronym;
    private String callTitle;
    private String callAcronym;
    private String yearDate;

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getCallTitle() {
        return callTitle;
    }

    public void setCallTitle(String callTitle) {
        this.callTitle = callTitle;
    }

    public String getCallAcronym() {
        return callAcronym;
    }

    public void setCallAcronym(String callAcronym) {
        this.callAcronym = callAcronym;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }



    public String getId() {
        //We consider an ORCID value to be unique based on the identifier retrieved from ORCID.
        final String nonDigestedIdentifier = HalAnrProjectAuthorityValue.class.toString() + " field, " + getCategory() + ", docid: " + getDocid();
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);
    }

    public AuthorityCategory getCategory() {
        return AuthorityCategory.ANR_PROJECT;
    }


    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getDocid()).toString();
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();

        doc.addField("label_title", getTitle());
        doc.addField("label_acronym", getAcronym());
        doc.addField("label_reference", getReference());
        doc.addField("label_calltitle", getCallTitle());
        doc.addField("label_callacronym", getCallAcronym());
        doc.addField("label_valid", getValid());
        doc.addField("label_yeardate", getYearDate());
        doc.addField("label_docid", getDocid());

        return doc;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        if (StringUtils.isNotBlank(getTitle())) {
            map.put("Titre", getTitle());
        } else {
            map.put("Titre", "/");
        }

        if (StringUtils.isNotBlank(getAcronym())) {
            map.put("Acronyme-du-projet", getAcronym());
        } else {
            map.put("Acronyme-du-projet", "/");
        }

        if (StringUtils.isNotBlank(getReference())) {
            map.put("Référence-du-project", getReference());
        } else {
            map.put("Référence-du-project", "/");
        }
        if (StringUtils.isNotBlank(getCallTitle())) {
            map.put("Intitulé-du-projet", getCallTitle());
        } else {
            map.put("Intitulé-du-projet", "/");
        }
        if (StringUtils.isNotBlank(getCallAcronym())) {
            map.put("Appel-à-projet", getCallAcronym());
        } else {
            map.put("Appel-à-projet", "/");
        }
        if (StringUtils.isNotBlank(getValid())) {
            map.put("Validité", getValid());
        } else {
            map.put("Validité", "/");
        }
        if (StringUtils.isNotBlank(getYearDate())) {
            map.put("Date-de-validité", getYearDate());
        } else {
            map.put("Date-de-validité", "/");
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
        final HalAnrProjectAuthorityValue otherValue = (HalAnrProjectAuthorityValue) object;
        return StringUtils.equals(this.getDocid(), otherValue.getDocid());
    }

    public int hashCode(){
        return 31 * getDocid().hashCode();
    }

    public boolean hasTheSameInformationAs(HalAnrProjectAuthorityValue other){
        return (StringUtils.equals(this.getValid(), other.getValid()) &&
                StringUtils.equals(this.getAcronym(), other.getAcronym()) &&
                StringUtils.equals(this.getReference(), other.getReference()) &&
                StringUtils.equals(this.getTitle(), other.getTitle()) &&
                StringUtils.equals(this.getCallAcronym(), other.getCallAcronym()) &&
                StringUtils.equals(this.getCallTitle(), other.getCallTitle()) &&
                StringUtils.equals(this.getYearDate(), other.getYearDate()) &&
                StringUtils.equals(this.getDocid(), other.getDocid())
        );
    }

    @Override
    public String getAuthorityType(){
        return "anr_project";
    }
}
