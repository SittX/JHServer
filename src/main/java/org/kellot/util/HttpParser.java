package org.kellot.util;

import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing incoming HTTP requests
 *
 * @author SittX
 */
public class HttpParser {
    private static final char SP = ' ';
    private static final char CR = '\r'; // Carriage return
    private static final char LF = '\n'; // Line feed
    private static final String QM = "\\?"; // question mark
    private final List<String> HTTP_METHODS = List.of("GET", "HEAD");
    private InputStreamReader input;
    private BufferedReader reader;

    public HttpParser(InputStreamReader input) {
        this.input = input;
        this.reader = new BufferedReader(input);
    }

    /**
     * Read data from the InputStream and bind the data (request line, headers, body) into the "HttpRequest" object.
     * It will read the data until it reaches to the end of the request headers.
     * Data are put into a List called "requestDetails".
     *
     * @return HttpRequest is an object containing all the information about the request.
     * @throws IOException
     * @throws UnsupportedHTTPMethodException
     */
    public HttpRequest parse() throws IOException, UnsupportedHTTPMethodException {
        HttpRequest request = new HttpRequest();
        List<String> requestDetails = new ArrayList<>();
        String currentLine;
        while (!(currentLine = reader.readLine()).equals("")) {
            requestDetails.add(currentLine);
        }

        if (requestDetails.size() <= 0) {
            // TODO Handle Bad Request here
            throw new RuntimeException();
        }

        // Remove the RequestLine from the RequestDetails List
        String requestLine = requestDetails.get(0);
        requestDetails.remove(0);

        parseRequestLine(requestLine, request);
        parseHeaders(requestDetails, request);
        // TODO Handle Request body parsing
//        parseBody();

        return request;
    }

    /**
     * Parse the request line in the HTTP request e.g ( GET /resource HTTP/1.1 )
     * It can also separate the query string data in the HTTP request if it is included in the request.
     * Data are then bind into the request object.
     *
     * @param requestLine
     * @throws IOException
     * @throws UnsupportedHTTPMethodException
     */
    private void parseRequestLine(String requestLine, HttpRequest request) throws IOException, UnsupportedHTTPMethodException {
        String[] requestLineParts = requestLine.split(" ");
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        validateHttpMethod(method);

        if (!path.contains(QM)) {
            request.setPath(path);
            request.setMethod(method);
            request.setHttpVersion(httpVersion);
            return;
        }

        parseQueryString(path, request);
    }

    /**
     * Separate resource path that including query string e.g ( /resource?username=kevin ) into
     * 1. Resource path ( /resource )
     * 2. query string ( username = kevin )
     * Data are then saved to the request object.
     *
     * @param resourcePath
     * @param request
     */
    private void parseQueryString(String resourcePath, HttpRequest request) {
        String[] resourcePathParts = resourcePath.split(QM);
        String destinationPath = resourcePathParts[0];
        String queryString = resourcePathParts[1];

        request.setPath(destinationPath);
        request.setQueryString(queryString);
    }

    /**
     * Validate if the incoming request is a valid request by checking on its request method.
     *
     * @param method is the HTTP request method of the incoming request e.g (GET, POST, etc)
     * @throws UnsupportedHTTPMethodException if the incoming request has invalid request or bad request method
     */
    private void validateHttpMethod(String method) throws UnsupportedHTTPMethodException {
        if (!HTTP_METHODS.contains(method)) {
            throw new UnsupportedHTTPMethodException("HTTP method is not supported.");
        }
    }

    /**
     * Parse data into Map<String,String> structure and save to the request object.
     *
     * @param requestHeaders is a list of string that contains headers data.
     * @param request
     */
    private void parseHeaders(List<String> requestHeaders, HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        for (String header : requestHeaders) {
            String[] headerParts = header.split(":");
            String type = headerParts[0];
            String value = headerParts[1];
            headers.put(type.trim(), value.trim());
        }
        request.setHeaders(headers);
    }

    public void parseBody(HttpRequest request) throws IOException {
// Check if the request has body or not
        String contentType = request.getHeaders().get("Content-Type").trim();
        int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));

        System.out.println(contentType.equals("application/x-www-form-urlencoded"));
        if (contentType.equals("application/x-www-form-urlencoded")) {
            System.out.println("Request is form-urlencoded");

            char[] buffer = new char[contentLength];
            input.read(buffer, 0, contentLength - 1);
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
