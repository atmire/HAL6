package com.atmire.authority.hal;

import org.dspace.authority.AuthorityValue;

/**
 * Created by jonas - jonas@atmire.com on 12/01/2018.
 */
public abstract class HALAuthorityValue extends AuthorityValue {

    private String docid;

    public void setDocid(String docid) {
        this.docid = docid;
    }
    public String getDocid() {
        return docid;
    }

    public String getHalIdentifier(){
       return docid;
    }
}
