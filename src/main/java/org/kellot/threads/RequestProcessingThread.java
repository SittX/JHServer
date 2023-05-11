package org.kellot.threads;import org.kellot.controller.FrontController;import org.kellot.exception.UnsupportedHTTPMethodException;import org.kellot.request.HttpRequest;import org.kellot.requestparser.HttpParser;import java.io.IOException;import java.io.InputStream;import java.io.OutputStream;import java.net.Socket;/** * A worker thread that is initiated to parse the request data * and pass to the controller. * * @author SittX */public class RequestProcessingThread extends Thread {    private final Socket socket;    public RequestProcessingThread(Socket socket) {        this.socket = socket;    }    /**     * Parse the incoming request data into "HttpRequest" class using the "HttpParser" utility class.     * The "HttpRequest" object will then be injected into the "FrontController" class to direct the request to different dispatcher.     */    @Override    public void run() {        try {            InputStream input = socket.getInputStream();            OutputStream output = socket.getOutputStream();            // TODO The sub-sequence HTTP requests will be sent in the same TCP stream.            // Should we do keep-alive here ?            HttpRequest request = new HttpParser(input).parse();            FrontController.getInstance().dispatchResponse(output, request);        } catch (IOException e) {            throw new RuntimeException(e);        } catch (UnsupportedHTTPMethodException e) {            throw new RuntimeException(e);        }    }}