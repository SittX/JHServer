package org.kellot.response;

import java.util.Map;

public class HttpResponse {
    private HttpResponseStatus status;
    private int statusCode;
    private String body;
    private Map<String, String> headers;

    HttpResponse(HttpResponseBuilder builder) {
        this.status = builder.getStatus();
        this.statusCode = builder.getCode();
        this.body = builder.getBody();
        this.headers = builder.getHeaders();
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        StringBuilder serverResponse = new StringBuilder();
        serverResponse.append("HTTP/1.1" + " " + statusCode + " " + status + "\r\n")
//                .append("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n")
                .append("Date: " + headers.get("Date") + "\r\n")
                .append("Content-Type: " + headers.get("Content-Type") + "\r\n")
                .append("Content-Length: " + headers.get("Content-Length") + "\r\n")
                .append("Server: Kevin server/0.8.4\r\n")
//                .append("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n")
                .append("Expires: " + headers.get("Expires") + "\r\n")
                .append("\r\n")
                .append(body);
        return serverResponse.toString();
    }

}
