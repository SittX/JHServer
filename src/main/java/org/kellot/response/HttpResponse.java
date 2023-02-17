package org.kellot.response;

import org.kellot.request.HttpStatus;

import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private String message;
    private String body;
    private Map<String,String> headers;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
