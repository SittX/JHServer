package org.kellot.threads;

import org.kellot.controller.FrontController;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpRequest;
import org.kellot.util.HttpParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * A worker thread that is initiated to parse the request data
 * and pass to the controller.
 * @author SittX
 */
public class RequestWorkerThread extends Thread {
    private Socket socket;

    public RequestWorkerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * Parse the incoming request data into "HttpRequest" class using the "HttpParser" utility class.
     * It will then pass the "HttpRequest" object into the "FrontController" to direct the request to different dispatcher.
     * Once the request has been parsed, the "HttpRequest" will be passed into the "FrontController" class
     * for further processing on the request.
     */
    @Override
    public void run() {
        try {
            InputStreamReader input = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            HttpRequest request = new HttpParser(input).parse();
            FrontController.getInstance().dispatchResponse(output, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedHTTPMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
