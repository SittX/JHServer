package org.kellot.requestparser;

import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpMethod;
import org.kellot.request.HttpRequest;
import org.kellot.requestparser.BodyParsingStrategy.BodyParsingContext;
import org.kellot.requestparser.BodyParsingStrategy.BodyParsingStrategy;
import org.kellot.requestparser.BodyParsingStrategy.FormDataBodyParsingStrategy;
import org.kellot.requestparser.BodyParsingStrategy.FormUrlEncodedBodyParsingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Utility class for parsing incoming HTTP requests
 *
 * @author SittX
 */
public class HttpParser {
    private static final String SPACE = " ";
    private static final String CR = "\r"; // Carriage return
    private static final String LF = "\n"; // Line feed
    private static final String QUESTION_MARK = "\\?"; // question mark
    private final BufferedReader input;
    private final HttpRequest request;

//    private final List<String> HTTP_METHODS = List.of("GET", "HEAD");

    public HttpParser(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
        this.request = new HttpRequest();
    }

    public static String getFileExtension(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }

    /**
     * Read data from the InputStream and bind the data (request line, headers, body) into the "HttpRequest" object.
     * It will read the data until it reaches to the end of the request headers.
     * Data are put into a List called "requestDetails".
     *
     * @return HttpRequest object which contains all the information about the request.
     * @throws IOException
     * @throws UnsupportedHTTPMethodException
     */
    public HttpRequest parse() throws IOException, UnsupportedHTTPMethodException {
        List<String> requestDetails = new ArrayList<>();
        String currentLine;
        while (!(currentLine = input.readLine()).equals("")) {
            requestDetails.add(currentLine);
        }

        if (requestDetails.size() == 0) {
            // TODO Handle Bad Request here
            throw new RuntimeException();
        }

        // Extract the RequestLine from the RequestDetails List and remove it
        String requestLine = requestDetails.get(0);
        requestDetails.remove(0);

        parseRequestLine(requestLine, request);
        parseHeaders(requestDetails, request);
        parseBody(request);

        return request;
    }

    /**
     * Parse the request line in the HTTP request e.g ( GET /resource HTTP/1.1 )
     * It can also separate the query string data in the HTTP request if it is included in the request.
     * Data are then bind into the request object.
     *
     * @throws UnsupportedHTTPMethodException
     */
    private void parseRequestLine(String requestLine, HttpRequest request) throws UnsupportedHTTPMethodException {
        String[] requestLineParts = requestLine.split(SPACE);
        String method = requestLineParts[0];
        String path = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        validateHttpMethod(method);

        if (!path.contains(QUESTION_MARK)) {
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
        String[] resourcePathParts = resourcePath.split(QUESTION_MARK);
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
        List<HttpMethod> methods = Arrays.stream(HttpMethod.values()).toList();
        if (!methods.contains(HttpMethod.valueOf(method))) {
            throw new UnsupportedHTTPMethodException("HTTP method is not supported.");
        }
    }

    /**
     * Parse data into Map<String,String> structure and save to the request object.
     * @param requestHeaders is a list of string that contains headers data.
     * @param request
     */
    private void parseHeaders(List<String> requestHeaders, HttpRequest request) {
        for (String header : requestHeaders) {
            String[] headerParts = header.split(":");
            String type = headerParts[0].trim();
            String value = headerParts[1].trim();
            request.setHeaders(type, value);
        }
    }


    // TODO Add the parsing result to the Request object
    public void parseBody(HttpRequest request) throws IOException {
        String contentType = request.getHeader("Content-Type");
        if (contentType.contains("application/x-www-form-urlencoded")) {
//            System.out.println("Request is form-urlencoded");
           var result = getBodyData(new FormUrlEncodedBodyParsingStrategy());
            System.out.println(result);
        } else if (contentType.contains("multipart/form-data")) {
//            System.out.println("Request is form-urlencoded");
            var result = getBodyData(new FormDataBodyParsingStrategy());
            System.out.println(result);
        }
    }

    private Map<String,String> getBodyData(BodyParsingStrategy strategy) throws IOException {
        BodyParsingContext bodyParsingContext = new BodyParsingContext(strategy);
        return bodyParsingContext.execute(request,input);
    }

}
