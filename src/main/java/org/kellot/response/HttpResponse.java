package org.kellot.response;

import java.util.Map;

public class HttpResponse {
    private static final String CRLF = "\r\n";
    private final HttpResponseStatus status;
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

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
        StringBuilder res = new StringBuilder();
        res.append("HTTP/1.1").append(" ").append(statusCode).append(" ").append(status).append(CRLF);

        // Add headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            res.append(header.getKey()).append(": ").append(header.getValue()).append(CRLF);
        }

        res.append(CRLF)
                .append(body);
        return res.toString();
    }

}
