package org.kellot.util;

import org.kellot.request.HttpRequest;
import org.kellot.request.UnsupportedHTTPMethodException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class contains methods and attributes that are related to an HttpRequest
// It will only store information and methods that can process this information
public class HttpParser {
    private final List<String> HTTP_METHODS = List.of("GET","HEAD");
    private InputStreamReader input;
    private HttpRequest request;
    private static final char SP = ' ';
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final String QM = "\\?";
    private BufferedReader reader;

    public HttpParser(InputStreamReader input) {
        this.input = input;
        this.request = new HttpRequest();
        this.reader  = new BufferedReader(input);
    }


    public HttpRequest parse() throws IOException, UnsupportedHTTPMethodException {

        String currentLine;
        List<String> requestDetails = new ArrayList<>();
        while(!(currentLine = reader.readLine()).equals("")){
            requestDetails.add(currentLine);
        }

        if(requestDetails.size() <= 0){
            // TODO Handle Bad Request here
            throw new RuntimeException();
        }

        String requestLine = requestDetails.get(0);
        // Remove the RequestLine from the RequestDetails List
        requestDetails.remove(0);

        parseRequestLine(requestLine);
        parseHeaders(requestDetails);
        parseBody();

        return request;
    }

    private void parseRequestLine(String requestLine) throws IOException, UnsupportedHTTPMethodException {
        String[] requestLineParts = requestLine.split(" ");
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];
        String queryString = null;

        validateHttpMethod(method);

        if(path.contains(QM)){
            String[] paths = path.split(QM);
            path = paths[0];
            queryString = paths[1];
        }

        request.setPath(path);
        request.setMethod(method);
        request.setHttpVersion(httpVersion);
        request.setQueryString(queryString);
    }

    private void validateHttpMethod(String method) throws UnsupportedHTTPMethodException {
        if(!HTTP_METHODS.contains(method)){
            throw new UnsupportedHTTPMethodException("HTTP method is not supported.");
        }
    }

    private void parseHeaders(List<String> requestHeaders){
        Map<String,String> headers = new HashMap<>();
        for (String header : requestHeaders) {
            String[] headerParts = header.split(":");
            String type = headerParts[0];
            String value = headerParts[1];
            headers.put(type,value);
        }
        request.setHeaders(headers);
    }

    public void parseBody(){}


}
