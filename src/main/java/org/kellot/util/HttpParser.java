package org.kellot.util;

import org.kellot.request.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// This class contains methods and attributes that are related to an HttpRequest
// It will only store information and methods that can process this information
public class HttpParser {

    // Parameter value -> GET /resource HTTP/1.1 (OR) GET /resource?value1=hello HTTP/1.1
//    public static Map<String, String> parseRequestLine(String requestLine) throws IOException {
//        Map<String, String> requestLineAttributes = new HashMap<>();
//
//        String[] requestHeaderParts = requestLine.split(" ");
//        requestLineAttributes.put("method", requestHeaderParts[0]);
//        requestLineAttributes.put("resource-path", requestHeaderParts[1]);
//        requestLineAttributes.put("http-version",requestHeaderParts[2]);
//
//        String resourcePath = requestLineAttributes.get("resource-path");
//        // Separate resource path from query string
//        if (resourcePath.contains("?")) {
//            requestLineAttributes.put("resource-path", resourcePath.split("\\?")[0]);
//            requestLineAttributes.put("query-string", resourcePath.split("\\?")[1]);
//        }
//        return requestLineAttributes;
//    }

    public static void parseRequestLine(String requestLine,HttpRequest request){
        String[] requestLines  = requestLine.split("\n")[0].split(" ");
        if(requestLines.length < 3){
            // TODO create a new HTTP exception
            throw new RuntimeException("Bad request");
        }

        request.setMethod(requestLines[0]);
        request.setHttpVersion(requestLines[2]);

        String path = requestLines[1];
        String queryString = null;
        // Separate resource path from query string
        if (path.contains("?")) {
            path = path.split("\\?")[0];
            queryString = path.split("\\?")[1];
        }

        request.setPath(path);
        request.setQueryString(queryString);
    }


    public static void parseBody(){}
    public static void parseHeaders(String header,HttpRequest request) throws IOException {
        var headerMaps = new HashMap<String,String>();
        String[] requestHeaders = header.split("\n");

        for (String requestHeader : requestHeaders) {
            if (requestHeader.contains(":")) {
                String[] headerParts = requestHeader.split(":");
                headerMaps.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }
        request.setHeaders(headerMaps);
    }

    public static String convertHeaderToCamelCase(String text){
        if(!text.contains("-")){
            return text.toLowerCase();
        }

        String[] words = text.split("-");
        StringBuilder result = new StringBuilder();
        result.append(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            result.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase());
        }
        return result.toString();
    }


    public Map<String, String> parseBody(String requestBody,HttpRequest request) throws IOException {
        String contentType = request.getHeaders().get("Content-Type");
        Map<String, String> dataMap = new HashMap<>();
        if (contentType.equals("application/x-www-form-urlencoded")) {
            String[] keyValuePairs = requestBody.split("&");
            for (String keyValuePair : keyValuePairs) {
                String[] parts = keyValuePair.split("=");
                dataMap.put(parts[0], parts[1]);
            }
        } else if (contentType.contains("multipart/form-data")) {
            String BOUNDARY_PREFIX = "--";
            String boundary = contentType.split("=")[1];
            String bodyEnding = BOUNDARY_PREFIX + boundary + BOUNDARY_PREFIX;

            String currentFieldName = "";
            String currentLine;
            while (!(currentLine = input.readLine()).equals(bodyEnding)) {
                if (currentLine.equals(BOUNDARY_PREFIX + boundary) || currentLine.isEmpty()) {
                    continue;
                } else if (currentLine.contains("Content-Disposition")) {
                    currentFieldName = currentLine.split("=")[1].replace("\"", "");
                } else {
                    if (currentLine.contains(BOUNDARY_PREFIX)) continue;
                    String value = currentLine.trim();
                    dataMap.put(currentFieldName, value);
                }
            }
        }
        dataMap.forEach((key, value) -> System.out.println(key + " : " + value));
        return dataMap;
    }

}
