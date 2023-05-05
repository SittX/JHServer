package org.kellot.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private String method;
    private String path;
    private String httpVersion;
    private String queryString;
    private final Map<String, String> headers;

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(String key,String value){
        this.headers.put(key,value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return Objects.equals(method, that.method) && Objects.equals(path, that.path) && Objects.equals(httpVersion, that.httpVersion) && Objects.equals(queryString, that.queryString) && Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path, httpVersion, queryString, headers);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", queryString='" + queryString + '\'' +
                ", headers=" + headers +
                '}';
    }
}
