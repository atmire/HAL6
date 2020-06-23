package com.atmire.compliance.rules.exception;

/**
 * Created by jonas - jonas@atmire.com on 15/01/2018.
 */
public class ValidationRuleDefinitionException extends Exception {

    public ValidationRuleDefinitionException(final String message, final Throwable ex) {
        super(message, ex);
    }

    public ValidationRuleDefinitionException(final String message) {
        super(message);
    }
}
