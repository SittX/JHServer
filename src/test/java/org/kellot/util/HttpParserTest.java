package org.kellot.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kellot.request.HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

class HttpParserTest {

    private HttpRequest request;

    @BeforeEach
    public void beforeEach() {
        request = new HttpRequest();
    }

    @AfterEach
    public void afterEach() {
        request = null;
    }

//    @Test
//    public void test_ParseRequest_LineCanParseGETRequest() throws IOException, UnsupportedHTTPMethodException {
//        String requestLine = "GET /resource HTTP/1.1";
//        HttpParser.parseRequestLine(getInputStream(requestLine), request);
//        assertEquals("GET", request.getMethod());
//        assertEquals("/resource", request.getPath());
//        assertEquals("HTTP/1.1", request.getHttpVersion());
//    }
//
//    @Test
//    public void test_ParseRequestLine_CanParseRequestWithQueryString() throws IOException, UnsupportedHTTPMethodException {
//        String requestLine = "GET /resource?username=kevin HTTP/1.1";
//        HttpParser.parseRequestLine(getInputStream(requestLine), request);
//        assertEquals("GET", request.getMethod());
//        assertEquals("/resource", request.getPath());
//        assertEquals("HTTP/1.1", request.getHttpVersion());
//        assertEquals("username=kevin", request.getQueryString());
//    }

    @Test
    void parseHeaders() {
    }

    @Test
    void parseBody() {
    }

    private InputStreamReader getInputStream(String requestLine) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(requestLine.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(byteStream);
        return inputStreamReader;
    }
}