package org.kellot.requestparser.BodyParsingStrategy;

import org.kellot.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class BodyParsingContext {
    private BodyParsingStrategy parsingStrategy;

    public BodyParsingContext(BodyParsingStrategy parsingStrategy) {
        this.parsingStrategy = parsingStrategy;
    }

    public void setParsingStrategy(BodyParsingStrategy parsingStrategy) {
        this.parsingStrategy = parsingStrategy;
    }

    public Map<String,String> execute(HttpRequest request, BufferedReader input) throws IOException {
       return this.parsingStrategy.execute(request,input);
    }
}
