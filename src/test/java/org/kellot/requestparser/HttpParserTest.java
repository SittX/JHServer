package org.kellot.requestparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kellot.exception.UnsupportedHTTPMethodException;
import org.kellot.request.HttpRequest;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpParserTest {
    @Test
    void parse_shouldReturnCorrectRequestBody() throws IOException, UnsupportedHTTPMethodException {
        String mockHttpRequest = "POST /upload HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "Content-Type: multipart/form-data; boundary=---------------------------1234567890\r\n\r\n" +
                "-----------------------------1234567890\r\n" +
                "Content-Disposition: form-data; name=\"username\"\r\n\r\n" +
                "\r\n" +
                "john_doe\r\n" +
                "-----------------------------1234567890\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\"\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "Hello, World!\r\n" +
                "-----------------------------1234567890--";

        InputStream input = new ByteArrayInputStream(mockHttpRequest.getBytes(StandardCharsets.UTF_8));
        HttpParser parser = new HttpParser(input);
        HttpRequest request = parser.parse();
        assertEquals("/upload",request.getPath());
        assertEquals("POST",request.getMethod());
        assertEquals("POST",request.getMethod());
    }


    @Test
    void parse_shouldGetCorrectQueryString() throws IOException, UnsupportedHTTPMethodException {
        String mockHttpRequest = "POST /upload?username=kevin?age=22 HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "Content-Type: multipart/form-data; boundary=---------------------------1234567890\r\n\r\n" +
                "-----------------------------1234567890\r\n" +
                "Content-Disposition: form-data; name=\"username\"\r\n\r\n" +
                "\r\n" +
                "john_doe\r\n" +
                "-----------------------------1234567890\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"example.txt\"\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "Hello, World!\r\n" +
                "-----------------------------1234567890--";
        InputStream input = new ByteArrayInputStream(mockHttpRequest.getBytes(StandardCharsets.UTF_8));
        HttpParser parser = new HttpParser(input);
        HttpRequest request = parser.parse();

        Map<String,String> mockQueryStrings = new HashMap<>();
        mockQueryStrings.put("username","kevin");
        mockQueryStrings.put("age","22");

        assertEquals(mockQueryStrings,request.getQueryString());
    }

    @Test
    void parseBody() {
    }
}