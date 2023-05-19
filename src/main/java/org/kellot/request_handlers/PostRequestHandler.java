package org.kellot.request_handlers;

import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponse;

public class PostRequestHandler implements RequestHandler{

    private final HttpRequest request;

    public PostRequestHandler(HttpRequest request) {
        this.request = request;
    }

    @Override
    public HttpResponse handleRequest() {
        return null;
    }
}
