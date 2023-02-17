package org.kellot.util;

import org.kellot.request.HttpRequest;
import org.kellot.exception.UnsupportedHTTPMethodException;

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
    private static final char SP = ' ';
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final String QM = "\\?";
    private final List<String> HTTP_METHODS = List.of("GET", "HEAD");
    private InputStreamReader input;
    private HttpRequest request;
    private BufferedReader reader;

    public HttpParser(InputStreamReader input) {
        this.input = input;
        this.request = new HttpRequest();
        this.reader = new BufferedReader(input);
    }


    public HttpRequest parse() throws IOException, UnsupportedHTTPMethodException {

        String currentLine;
        List<String> requestDetails = new ArrayList<>();
        while (!(currentLine = reader.readLine()).equals("")) {
            requestDetails.add(currentLine);
        }

        if (requestDetails.size() <= 0) {
            // TODO Handle Bad Request here
            throw new RuntimeException();
        }

        String requestLine = requestDetails.get(0);
        // Remove the RequestLine from the RequestDetails List
        requestDetails.remove(0);

        parseRequestLine(requestLine);
        parseHeaders(requestDetails);

        // TODO Handle Request body parsing
//        parseBody();

        return request;
    }

    private void parseRequestLine(String requestLine) throws IOException, UnsupportedHTTPMethodException {
        String[] requestLineParts = requestLine.split(" ");
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];
        String queryString = null;

        validateHttpMethod(method);

        if (path.contains(QM)) {
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
        if (!HTTP_METHODS.contains(method)) {
            throw new UnsupportedHTTPMethodException("HTTP method is not supported.");
        }
    }

    private void parseHeaders(List<String> requestHeaders) {
        Map<String, String> headers = new HashMap<>();
        for (String header : requestHeaders) {
            String[] headerParts = header.split(":");
            String type = headerParts[0];
            String value = headerParts[1];
            headers.put(type.trim(), value.trim());
        }
        request.setHeaders(headers);
    }

    public void parseBody() throws IOException {
// Check if the request has body or not
        String contentType = request.getHeaders().get("Content-Type").trim();
        int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));

        System.out.println(contentType.equals("application/x-www-form-urlencoded"));
        if (contentType.equals("application/x-www-form-urlencoded")) {
            System.out.println("Request is form-urlencoded");

            char[] buffer = new char[contentLength];
            input.read(buffer,0,contentLength -1 );
            String body = new String(buffer);

            String[] bodyParts = body.toString().split("&");
            for (String part : bodyParts) {
                String[] data = part.split("=");
            }

        } else if (contentType.equals("multipart/form-data")) {
            System.out.println("Request is form-urlencoded");
            String BOUNDARY_PREFIX = "--";
            String boundary = contentType.split("=")[1];
            String bodyEnding = BOUNDARY_PREFIX + boundary + BOUNDARY_PREFIX;

            String currentFieldName = "";
            String current;
            while (!(current = reader.readLine()).equals(bodyEnding)) {
                if (current.equals(BOUNDARY_PREFIX + boundary) || current.isEmpty()) {
                    continue;
                } else if (current.contains("Content-Disposition")) {
                    currentFieldName = current.split("=")[1].replace("\"", "");
                } else {
                    if (current.contains(BOUNDARY_PREFIX)) continue;
                    String value = current.trim();
//                            dataMap.put(currentFieldName, value);
                }
            }
        }
    }


}
