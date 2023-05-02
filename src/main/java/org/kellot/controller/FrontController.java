package org.kellot.controller;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.dispatcher.RequestDispatcher;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpMethod;
import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponseStatus;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

// TODO This class is responsible for redirecting the request to its related dispatcher
// TODO It is also responsible for check if the request is valid or not ( Methods, Request URL ) and dispatch error handler

/**
 * A singleton controller class responsible for redirecting the requests to their related dispatcher
 * based on their resource path and method.
 *
 * @author SittX
 */
public class FrontController {
    private static FrontController requestController;
    private final ServerConfiguration conf;

    private FrontController() {
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    public static FrontController getInstance() {
        if (requestController == null) {
            requestController = new FrontController();
        }
        return requestController;
    }

    public void dispatchResponse(BufferedOutputStream outputStream, HttpRequest request) throws UnsupportedHTTPMethodException {
        RequestDispatcher dispatcher = new RequestDispatcher(outputStream);

        if (!validateHttpMethod(request)) {
            dispatcher.dispatchError(HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else if (!validateQueryStringLength(request)) {
            dispatcher.dispatchError(HttpResponseStatus.BAD_REQUEST);
        } else {
            dispatcher.dispatchResponse(request);
        }
    }

    private boolean validateQueryStringLength(HttpRequest request) {
        if (request.getQueryString() != null) {
            return request.getQueryString().length() <= conf.queryStringLength();
        }
        return true;
    }

    /**
     * Validate Http method of the request.
     *
     * @param request
     * @return TRUE if the method is valid and FALSE if it is invalid.
     */
    private boolean validateHttpMethod(HttpRequest request) {
        String requestMethod = request.getMethod();
        for (HttpMethod method : HttpMethod.values()) {
            if (HttpMethod.valueOf(requestMethod) == method) {
                return true;
            }
        }
        return false;
    }

}
