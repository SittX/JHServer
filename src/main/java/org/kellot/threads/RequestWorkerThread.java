package org.kellot.threads;

import org.kellot.controller.FrontController;
import org.kellot.request.HttpRequest;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.util.HttpParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestWorkerThread extends Thread {
    private Socket socket;

    public RequestWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader input = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8);
            HttpRequest request = new HttpParser(input).parse();
            FrontController.getInstance().dispatchRequest(output,request);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedHTTPMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
