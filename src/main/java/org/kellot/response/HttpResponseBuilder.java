package org.kellot.response;

import java.util.Map;

public class HttpResponseBuilder {
    private HttpResponseStatus status;
    private int code;
    private String body;
    private Map<String, String> headers;

    public HttpResponseBuilder(HttpResponseStatus status, int code) {
        this.status = status;
        this.code = code;
    }

    public HttpResponse build() {
        return new HttpResponse(this);
    }

    HttpResponseStatus getStatus() {
        return status;
    }

    int getCode() {
        return code;
    }

    String getBody() {
        return body;
    }

    public HttpResponseBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponseBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
