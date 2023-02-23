package org.kellot.config;

public class ServerConfiguration {
    private int port;
    private String rootLocation;
    private String pageLocation;
    private String errorTemplateLocation;
    private int queryStringLength;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootLocation() {
        return rootLocation;
    }

    public void setRootLocation(String rootLocation) {
        this.rootLocation = rootLocation;
    }

    public String getPageLocation() {
        return pageLocation;
    }

    public void setPageLocation(String pageLocation) {
        this.pageLocation = pageLocation;
    }

    public String getErrorTemplateLocation() {
        return errorTemplateLocation;
    }

    public void setErrorTemplateLocation(String errorTemplateLocation) {
        this.errorTemplateLocation = errorTemplateLocation;
    }

    public int getQueryStringLength() {
        return queryStringLength;
    }

    public void setQueryStringLength(int queryStringLength) {
        this.queryStringLength = queryStringLength;
    }
}
