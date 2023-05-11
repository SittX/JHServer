package org.kellot.requestparser.BodyParsingStrategy;

import org.kellot.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FormDataBodyParsingStrategy implements BodyParsingStrategy {
    private final String BOUNDARY_CHAR = "--";

    @Override
    public Map<String,String> execute(HttpRequest request, BufferedReader input) throws IOException {
        String contentType = request.getHeader("Content-Type");
//        String BOUNDARY_VALUE = contentType.split("=")[1];
        String BOUNDARY_VALUE = contentType.substring(contentType.lastIndexOf("=") + 1);
        String requestBodyEnding = BOUNDARY_CHAR + BOUNDARY_VALUE + BOUNDARY_CHAR;
        String boundaryDivider = BOUNDARY_CHAR + BOUNDARY_VALUE;
        String contentDispositionHeader = "Content-Disposition: form-data; ";

        StringBuilder requestBodyBuilder = new StringBuilder();
        String current;
        while (!(current = input.readLine()).equals(requestBodyEnding)) {
            requestBodyBuilder.append(current);
        }

        String requestBody = requestBodyBuilder.toString();
        String[] values = requestBody.split(boundaryDivider+contentDispositionHeader);
        // We have to copy this again because splitting the request body results
        // some weird character in the array first index.
        values = Arrays.copyOfRange(values,1,values.length);

        // Convert this -> name="username"kevin -> key:username , value:kevin
        Map<String,String> resultSet = new HashMap<>();
        for(String value :values){
            String fieldName = value.substring(value.indexOf("\"")+1 , value.lastIndexOf("\""));
            String fieldValue = value.substring(value.lastIndexOf("\"")+1);
            resultSet.put(fieldName,fieldValue);
        }
       return resultSet;
    }

//    private Map<String,String> testingMethod(HttpRequest request,BufferedReader input) throws IOException {
//       Map<String,String> resultSet = new HashMap<>();
//        String contentType = request.getHeader("Content-Type");
//        String boundary = contentType.split("=")[1];
//        String BOUNDARY_SUFFIX = "--";
//        String bodyEnding = BOUNDARY_SUFFIX + boundary + BOUNDARY_SUFFIX;
//        String boundaryLine = BOUNDARY_SUFFIX + boundary;
//
//        String current;
//        String fieldAttributes;
//        while (!(current = input.readLine()).equals(bodyEnding)) {
//            if (current.equals(boundaryLine) || current.isEmpty()) {
//                continue;
//            }
//
//            if(current.contains("Content-Disposition")){
//                String formData = current.substring(current.indexOf("Content-Disposition: form-data; ") + 1);
////                String[] fieldAttributes = formData.split("=");
//                continue;
//            }
//
//
////            else if (current.contains("Content-Disposition")) {
////                String formData = current.substring(current.indexOf(';')+1);
////                String[] formKeyValues= formData.split("; ");
////                for(String formKeyValue : formKeyValues){
////                    String[] keyValues = formKeyValue.split(";");
////                    String key = keyValues[0];
////                    String value = keyValues[1];
////                    resultSet.put(key,value);
////                }
////            }
//        }
//
//        return  new HashMap<>();
//    }

}
