package org.kellot.threads;

import org.kellot.request.HttpRequest;
import org.kellot.request.UnsupportedHTTPMethodException;
import org.kellot.util.HttpParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestWorker extends Thread {

    private Socket socket;

    public RequestWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Reached to request worker thread.");
        System.out.println(socket.getLocalAddress());

        try {
            InputStreamReader input = new InputStreamReader(socket.getInputStream(), "UTF-8");

            HttpRequest request = new HttpParser(input).parse();
            System.out.println(request.getMethod());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedHTTPMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
