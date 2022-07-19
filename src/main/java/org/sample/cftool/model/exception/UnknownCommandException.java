package org.sample.cftool.model.exception;

public class UnknownCommandException extends Exception {

    public UnknownCommandException(final String message) {
        super(message);
    }
}
