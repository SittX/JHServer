package org.kellot.request;

public class UnsupportedHTTPMethodException extends Exception {
    public UnsupportedHTTPMethodException(String message) {
        super(message);
    }
}
