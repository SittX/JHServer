package org.kellot.controller;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.dispatcher.RequestDispatcher;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpMethod;
import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * A singleton controller class responsible for redirecting the requests to their corresponding dispatcher methods
 * based on their HTTP methods.
 *
 * All the authentication, request validation and security checking will be happening in this class.
 * The response will also be sent back from this class which enables to check every response from the server in one place.
 *
 * @author SittX
 */
public class FrontController {
    private static FrontController requestController;
    private final ServerConfiguration config;
    private FrontController() {
        this.config = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    public static FrontController getInstance() {
        if (requestController == null) {
            requestController = new FrontController();
        }
        return requestController;
    }

    public void handleRequest(OutputStream outputStream, HttpRequest request) throws UnsupportedHTTPMethodException {
        RequestDispatcher dispatcher = new RequestDispatcher(request,outputStream);

        // Middlewares
        if (!validateHttpMethod(request)) {
            dispatcher.dispatchError(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        if (!validateQueryStringLength(request)) {
            dispatcher.dispatchError(HttpResponseStatus.BAD_REQUEST);
            return;
        }

        /**
         * Calling the dispatch method should return an HttpResponse Object
         * This allows the FrontController class to have a control over what is received from and send to the clients.
         */
        HttpResponse response = dispatcher.dispatchRequest();
        String reqAccept = request.getHeaders().get("Accept");
        String requestPath = request.getPath();
        String fileExtension = requestPath.substring(requestPath.lastIndexOf('.') + 1).toUpperCase();

        // Warning !!!
        // Don't touch this section
        if (reqAccept.startsWith("image/")) {
            try {
                BufferedImage bufferedImage = response.getBufferedImage();
                ImageIO.write(bufferedImage, fileExtension, outputStream);
                outputStream.close();
                return;
            } catch (IOException e) {
                // Will handle the exception later ;)
                throw new RuntimeException(e);
            }
        }
        // Section end

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(response.toString());
            writer.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateQueryStringLength(HttpRequest request) {
        if (request.getQueryString() != null) {
            return request.getQueryString().size() <= config.queryStringLength();
        }
        return true;
    }

    /**
     * Validate if the request HTTP method is supported in the server and is a valid one.
     * @param request
     * @return TRUE if the method is valid and FALSE if it is an invalid request.
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
