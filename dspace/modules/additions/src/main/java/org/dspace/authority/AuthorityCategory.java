package org.dspace.authority;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lotte.hofstede at atmire.com
 */
public enum AuthorityCategory {
    PERSON, JOURNAL, DOMAIN, EUROPEAN_PROJECT, ANR_PROJECT;


    public static AuthorityCategory fromString(String string) {
        if (StringUtils.isNotBlank(string)) {
            return AuthorityCategory.valueOf(string.toUpperCase());
        }
        return null;
    }
    public String toString() {
        return StringUtils.capitalize(this.name().toLowerCase());
    }
}
