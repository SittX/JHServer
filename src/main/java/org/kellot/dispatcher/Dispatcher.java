package org.kellot.dispatcher;

import org.kellot.ResourceManager;
import org.kellot.request.HttpRequest;
import org.kellot.request.HttpStatus;
import org.kellot.response.HttpResponse;

import java.io.*;

public class Dispatcher {

    private OutputStreamWriter output;

    public Dispatcher(OutputStreamWriter output) {
        this.output = output;
    }

    public void dispatch(HttpRequest request){
        // TODO Separate the dispatching logics
        // TODO Find a better way to construct response object with dynamic datetime and content
        String path = request.getPath();
        ResourceManager resourceManager = ResourceManager.getInstance();
        boolean result = resourceManager.searchPage(path);

        if(result){
            try {
                byte[] htmlCode = resourceManager.getPage(path);
                // Construct response object
                HttpResponse response = new HttpResponse();
                response.setStatus(HttpStatus.OK);
                // Send response to the client
                StringBuilder serverResponse = new StringBuilder();
                serverResponse.append("HTTP/1.1 200 OK\r\n")
                        .append("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n")
                        .append("Content-Type: text/html\r\n")
                        .append("Content-Length: " + htmlCode.length + "\r\n")
                        .append("Server: Kevin server/0.8.4\r\n")
                        .append("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n")
                        .append("\r\n")
                        .append(new String(htmlCode));

                output.write(serverResponse.toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
