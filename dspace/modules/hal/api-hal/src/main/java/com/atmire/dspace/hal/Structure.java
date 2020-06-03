package com.atmire.dspace.hal;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by: Antoine Snyers (antoine at atmire dot com)
 * Date: 06 Oct 2014
 */
public class Structure {

    protected int id;
    protected String name;

    public Structure(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .toString();
    }
}
