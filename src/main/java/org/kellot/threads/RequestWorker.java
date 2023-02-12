package org.kellot.threads;

import org.kellot.request.HttpRequest;
import org.kellot.request.HttpRequestManager;

import java.io.*;
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

        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            HttpRequestManager.getHttpRequest(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String htmlCode = "<html><body><h1>Hello world</h1></body></html>";

        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK\r\n")
                .append("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n")
                .append("Content-Type: text/html\r\n")
                .append("Content-Length: " + htmlCode.length() + "\r\n")
                .append("Server: Kevin server/0.8.4\r\n")
                .append("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n")
                .append("\r\n")
                .append(htmlCode);

        String response = builder.toString();
        try {
            output.write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
