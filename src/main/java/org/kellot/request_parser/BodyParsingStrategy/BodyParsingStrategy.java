package org.kellot.request_parser.BodyParsingStrategy;

import org.kellot.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public interface BodyParsingStrategy {
    Map<String,String> execute(HttpRequest request, BufferedReader input) throws IOException;
}
