package org.kellot.util;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpRequestParserTest {

//    @Test
//    void test_RequestLineCanParseWithQueryString() {
//        Map<String,String> testCases = new HashMap<>();
//        testCases.put("method","GET");
//        testCases.put("resource-path","/resource");
//        testCases.put("query-string","username=kevin");
//        testCases.put("http-version","HTTP/1.1");
//        try {
//            var result = HttpRequestParser.parseRequestLine("GET /resource?username=kevin HTTP/1.1");
//            assertTrue(result.equals(testCases));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    void test_RequestLineCanParseWithoutQueryString(){
//        Map<String,String> testCases = new HashMap<>();
//        testCases.put("method","GET");
//        testCases.put("resource-path","/resource");
//        testCases.put("http-version","HTTP/1.1");
//        try {
//            var result = HttpRequestParser.parseRequestLine("GET /resource HTTP/1.1");
//            assertTrue(result.equals(testCases));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Test
//    public void test_ParseHeadersCanParseHeaders() {
//        Map<String, String> testCases = new HashMap<>();
//        testCases.put("method", "GET");
//        testCases.put("resourcePath", "/index.html");
//        testCases.put("httpVersion", "HTTP/1.1");
//        testCases.put("host", "www.example.com");
//        testCases.put("userAgent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36");
//        testCases.put("acceptLanguage", "en-US,en;q=0.9");
//        testCases.put("acceptEncoding", "gzip, deflate");
//        testCases.put("connection", "keep-alive");
//        testCases.put("cacheControl", "max-age=0");
//
//        try {
//            var result = HttpParser.parseHeaders("GET /index.html HTTP/1.1\n" +
//                    "Host: www.example.com\n" +
//                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36\n" +
//                    "Accept-Language: en-US,en;q=0.9\n" +
//                    "Accept-Encoding: gzip, deflate\n" +
//                    "Connection: keep-alive\n" +
//                    "Cache-Control: max-age=0\n");
//
//            assertTrue(result.equals(testCases));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    public void test_ConvertToCamelCase(){
        HttpParser parser = new HttpParser();
        String result = parser.convertHeaderToCamelCase("User-Agent");
        System.out.println(result);
        assertEquals(result,"userAgent");
    }
}