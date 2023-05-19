package org.kellot.request_handlers;

import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponse;

public class DeleteRequestHandler implements RequestHandler {
    private final HttpRequest request;
    public DeleteRequestHandler(HttpRequest request) {
        this.request = request;
    }

    @Override
    public HttpResponse handleRequest() {
        System.out.println(request.toString());
        return null;
    }
}
