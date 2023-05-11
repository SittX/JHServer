package org.kellot.controller;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.dispatcher.RequestDispatcher;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpMethod;
import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponseStatus;

import java.io.OutputStream;


/**
 * A singleton controller class responsible for redirecting the requests to their corresponding dispatcher methods
 * based on their HTTP methods.
 * This class is also validate the request before sending it to the dispatcher class.
 *
 * @author SittX
 */
public class FrontController {
    private static FrontController requestController;
    private final ServerConfiguration conf;

    // Instantiating ServerConfigurationManager object without initializing could throw an exception.
    // We might have to do something about it.
    private FrontController() {
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    public static FrontController getInstance() {
        if (requestController == null) {
            requestController = new FrontController();
        }
        return requestController;
    }

    public void dispatchResponse(OutputStream outputStream, HttpRequest request) throws UnsupportedHTTPMethodException {
        RequestDispatcher dispatcher = new RequestDispatcher(outputStream);

        if (!validateHttpMethod(request)) {
            dispatcher.dispatchError(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        if (!validateQueryStringLength(request)) {
            dispatcher.dispatchError(HttpResponseStatus.BAD_REQUEST);
            return;
        }

        dispatcher.dispatchResponse(request);
    }

    private boolean validateQueryStringLength(HttpRequest request) {
        if (request.getQueryString() != null) {
            return request.getQueryString().size() <= conf.queryStringLength();
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
