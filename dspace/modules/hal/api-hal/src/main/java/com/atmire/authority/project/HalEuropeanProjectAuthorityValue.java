package com.atmire.authority.project;

import com.atmire.authority.hal.HALAuthorityValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityCategory;
import org.dspace.authority.AuthorityKeyRepresentation;

import java.util.Map;

public class HalEuropeanProjectAuthorityValue extends HALAuthorityValue {

    private String title;
    private String reference;
    private String finance;
    private String valid;
    private String startdate;
    private String enddate;

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

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getStartDate() {
        return startdate;
    }

    public void setStartDate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndDate() {
        return enddate;
    }

    public void setEndDate(String enddate) {
        this.enddate = enddate;
    }


    public String getId() {
        //We consider an ORCID value to be unique based on the identifier retrieved from ORCID.
        final String nonDigestedIdentifier = HalEuropeanProjectAuthorityValue.class.toString() + " field, " + getCategory() + ", docid: " + getDocid();
        // We return an md5 digest of the toString, this will ensure a unique identifier for the same value each time
        return DigestUtils.md5Hex(nonDigestedIdentifier);
    }

    public AuthorityCategory getCategory() {
        return AuthorityCategory.EUROPEAN_PROJECT;
    }


    @Override
    public String generateString() {
        return new AuthorityKeyRepresentation(getAuthorityType(), getDocid()).toString();
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();

        doc.addField("label_title", getTitle());
        doc.addField("label_reference", getReference());
        doc.addField("label_finance", getFinance());
        doc.addField("label_valid", getValid());
        doc.addField("label_startdate", getStartDate());
        doc.addField("label_enddate", getEndDate());
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

        if (StringUtils.isNotBlank(getReference())) {
            map.put("Référence-du-project", getReference());
        } else {
            map.put("Référence-du-project", "/");
        }

        if (StringUtils.isNotBlank(getFinance())) {
            map.put("Financement-du-project", getFinance());
        } else {
            map.put("Financement-du-project", "/");
        }

        if (StringUtils.isNotBlank(getValid())) {
            map.put("État-du-projet", getValid());
        } else {
            map.put("État-du-projet", "/");
        }

        if (StringUtils.isNotBlank(getStartDate())) {
            map.put("Date-de-début-du-projet", getStartDate());
        } else {
            map.put("Date-de-début-du-projet", "/");
        }

        if (StringUtils.isNotBlank(getEndDate())) {
            map.put("Date-de-fin-du-projet", getEndDate());
        } else {
            map.put("Date-de-fin-du-projet", "/");
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
        final HalEuropeanProjectAuthorityValue otherValue = (HalEuropeanProjectAuthorityValue) object;
        return StringUtils.equals(this.getDocid(), otherValue.getDocid());
    }

    public int hashCode(){
        return 31 * getDocid().hashCode();
    }

    public boolean hasTheSameInformationAs(HalEuropeanProjectAuthorityValue other){
        return (StringUtils.equals(this.getValid(), other.getValid()) &&
                StringUtils.equals(this.getFinance(), other.getFinance()) &&
                StringUtils.equals(this.getReference(), other.getReference()) &&
                StringUtils.equals(this.getTitle(), other.getTitle()) &&
                StringUtils.equals(this.getStartDate(), other.getStartDate()) &&
                StringUtils.equals(this.getEndDate(), other.getEndDate()) &&
                StringUtils.equals(this.getDocid(), other.getDocid())
        );
    }

    @Override
    public String getAuthorityType(){
        return "european_project";
    }
}
