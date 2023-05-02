package org.kellot;

public enum MIME_TYPE {
    CSS("text/css"),
    JS("text/js"),
    HTML("text/html"),
    JPG("image/jpg"),
    PNG("image/png"),
    ICO("image/x-icon");
    private final String contentTypeString;

    public String getContentTypeString() {
        return contentTypeString;
    }

    MIME_TYPE(String contentType) {
        this.contentTypeString = contentType;
    }
}
