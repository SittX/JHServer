package org.kellot.request_parser.BodyParsingStrategy;

import org.kellot.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormUrlEncodedBodyParsingStrategy implements BodyParsingStrategy {
    @Override
    public Map<String,String> execute(HttpRequest request, BufferedReader input) throws IOException {
        Map<String,String> resultSet = new HashMap<>();
        int contentLength = Integer.parseInt(request.getHeader("Content-Length"));

        char[] buffer = new char[contentLength];
        input.read(buffer, 0, contentLength - 1);

        String body = new String(buffer);
        String[] bodyParts = body.split("&");
        for (String part : bodyParts) {
            System.out.println(part);
            String[] field = part.split("=");
            String key = field[0];
            String value = field[1];
            resultSet.put(key,value);
        }
        return resultSet;
    }
}
