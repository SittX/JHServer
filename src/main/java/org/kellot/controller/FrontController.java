package org.kellot.controller;

import org.kellot.dispatcher.Dispatcher;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpRequest;

import java.io.OutputStreamWriter;

// TODO This class is responsible for redirecting the request to its related dispatcher
// TODO It is also responsible for check if the request is valid or not ( Methods, Request URL ) and dispatch error handler
public class FrontController {
    private static FrontController requestController;
    private FrontController(){}

    public static FrontController getInstance(){
        if(requestController == null){
            requestController = new FrontController();
        }
        return requestController;
    }

    public void dispatchRequest(OutputStreamWriter outputStream, HttpRequest request) throws UnsupportedHTTPMethodException {
        Dispatcher dispatcher = new Dispatcher(outputStream);

        // TODO Check Request Method
        // TODO Check Request URL
        if(valiateHttpMethod(request)){
            throw new UnsupportedHTTPMethodException("HTTP request method is not supported");
        }else if(validateRequestURL(request)){
            throw new RuntimeException("Request URL is too long");
        }else{
            dispatcher.dispatch(request);
        }

    }

    private boolean validateRequestURL(HttpRequest request) {
        return false;
    }

    private boolean valiateHttpMethod(HttpRequest request) {
        return false;
    }

}
