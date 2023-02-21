package org.kellot.response;

public enum HttpResponseStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    URI_TOO_LARGE(414),
    UNSUPPORTED_MEDIA_TYPE(415),
   METHOD_NOT_ALLOWED(405),
    NOT_IMPLEMENTED(501);

    private int statusCode;

    HttpResponseStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return this.statusCode;
    }
}