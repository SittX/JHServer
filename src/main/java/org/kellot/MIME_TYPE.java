package org.kellot;

public enum MIME_TYPE {
    CSS("text/css"),
    JS("text/javascript"),
    HTML("text/html"),
    JPG("image/jpg"),
    PNG("image/png"),
    ICO("image/x-icon");
    private final String contentTypeString;

    MIME_TYPE(String contentType) {
        this.contentTypeString = contentType;
    }

    public String getContentTypeString() {
        return contentTypeString;
    }
}
