package org.kellot.request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.kellot.util.HttpParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestManager {

    public static void getHttpRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String currentLine;
        while (!(currentLine = reader.readLine()).equals("")) {
            sb.append(currentLine).append("\n");
        }

        HttpRequest request = new HttpRequest();
        // Parse the request data from client socket
        HttpParser.parseRequestLine(sb.toString(),request);
//        HttpParser.parseHeaders(sb.toString());

    }

}
