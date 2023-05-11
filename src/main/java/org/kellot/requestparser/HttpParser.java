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
    private final BufferedReader input;
    private final HttpRequest request;

    public HttpParser(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
        this.request = new HttpRequest();
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
        List<String> requestHeaders = new ArrayList<>();
        String currentLine;
        while (!Objects.equals(currentLine = input.readLine(), "")) {
            requestHeaders.add(currentLine);
        }

        if (requestHeaders.size() == 0) {
            // TODO Handle Bad Request here
            throw new RuntimeException();
        }

        // Extract the RequestLine from the RequestDetails List and remove it
        String requestLine = requestHeaders.remove(0);
        parseRequestLine(requestLine, request);
        parseHeaders(requestHeaders, request);
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
        String[] requestLineParts = requestLine.split(" ");
        String httpMethod = requestLineParts[0];
        String requestPath = requestLineParts[1];
        String httpVersion = requestLineParts[2];

        request.setMethod(httpMethod);
        request.setHttpVersion(httpVersion);

        validateHttpMethod(httpMethod);

        // TODO Error found here !! This condition is evaluated True even there is a query string
        if (requestPath.contains("?")) {
            request.setPath(requestPath.substring(0,requestPath.indexOf("?")));
            parseQueryString(requestPath, request);
            return;
        }

        request.setPath(requestPath);
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

    /**
     * Separate resource path that including query string e.g ( /resource?username=kevin ) into
     * 1. Resource path ( /resource )
     * 2. query string ( username = kevin )
     * Data are then saved to the request object.
     * @param resourcePath
     * @param request
     */
    private void parseQueryString(String resourcePath, HttpRequest request) {
        String queryString = resourcePath.substring(resourcePath.indexOf("?") + 1);
        // Single key-value pair -> name=kevin
        if(!queryString.contains("?")){
            addQueryStringToRequest(queryString,request);
            return;
        }

        // Multiple key-value pairs -> name=kevin?age=21
        String[] keyValues = queryString.split("\\?");
        for(String keyValue : keyValues){
            addQueryStringToRequest(keyValue,request);
        }
    }

    private void addQueryStringToRequest(String queryString, HttpRequest request){
        String key = queryString.substring(0,queryString.indexOf("="));
        String value = queryString.substring(queryString.indexOf("=") +1);
        request.setQueryString(key,value);
    }

    /**
     * Validate if the incoming request is a valid request by checking on its request method.
     * @param method is the HTTP request method of the incoming request e.g (GET, POST, etc)
     * @throws UnsupportedHTTPMethodException if the incoming request has invalid request or bad request method
     */
    private void validateHttpMethod(String method) throws UnsupportedHTTPMethodException {
        List<HttpMethod> methods = Arrays.stream(HttpMethod.values()).toList();
        if (!methods.contains(HttpMethod.valueOf(method))) {
            throw new UnsupportedHTTPMethodException("HTTP method is not supported.");
        }
    }

    // TODO Add the parsing result to the Request object
    public void parseBody(HttpRequest request) throws IOException {
        String contentType = request.getHeader("Content-Type");

        if(contentType == null)
            return;

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
