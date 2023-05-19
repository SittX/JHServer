package org.kellot.request_handlers;

import org.kellot.MIME_TYPE;
import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;
import org.kellot.resource.ResourceManager;
import org.kellot.resource.ResponseData;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseBuilder;
import org.kellot.response.HttpResponseStatus;
import org.kellot.util.HttpDate;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;

public class GetRequestHandler implements RequestHandler {
    private final ResourceManager resourceManager;
    private final ServerConfiguration conf;
    private final HttpRequest request;

    public GetRequestHandler(HttpRequest request) {
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
        this.resourceManager= new ResourceManager();
        this.request = request;
    }

    public HttpResponse handleRequest() {
        String reqAcceptType = request.getHeaders().get("Accept");
        String reqContentType = reqAcceptType != null ?  reqAcceptType.split("/")[0]:null;
        String requestPath = request.getPath();
        String fileExtension = requestPath.substring(requestPath.lastIndexOf('.') + 1).toUpperCase();
        String contentType = getContentTypeForFileExtension(fileExtension);
        String filePath = conf.pageLocation() + requestPath;

        ResponseData responseData;
        if(reqContentType.equals("image")) {
            BufferedImage image = resourceManager.fetchImageData(filePath);
            responseData = new ResponseData();
            responseData.setBufferedImage(image);
//                if(fileExtension.equalsIgnoreCase("ico")){
//                   yield resourceManager.fetchIcoImage(filePath);
//                }
        }else{
            responseData = resourceManager.fetchFileData(filePath);
        }

        if (!responseData.isValid()) {
            throw new RuntimeException();
        }

        // Todo Check if the response data is bufferedImage or byte[]
        if(reqContentType.equals("image")){
            BufferedImage bufferedImage = responseData.getBufferedImage();
            HttpResponse res = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                    .setHeader("Connection", "keep-alive")
                    .setHeader("Content-Type",contentType)
                    .setHeader("Date", HttpDate.setCurrentDateTime())
                    .setHeader("Expires", HttpDate.setExpiryDateTime(1))
                    .setHeader("Server", "Kev JHServer version 1")
                    .build();
            res.setBufferedImage(bufferedImage);
            return res;
        }

            byte[] resultData = responseData.getData();
            HttpResponse res = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                    .setBody(new String(resultData))
                    .setHeader("Connection", "keep-alive")
                    .setHeader("Content-Type", contentType)
                    .setHeader("Content-Length", String.valueOf(resultData.length))
                    .setHeader("Date", HttpDate.setCurrentDateTime())
                    .setHeader("Expires", HttpDate.setExpiryDateTime(1))
                    .setHeader("Server", "Kev JHServer version 1")
                    .build();

            return res;
    }

    private String getContentTypeForFileExtension(String fileExtension) {
        for (MIME_TYPE type : MIME_TYPE.values()) {
            if (MIME_TYPE.valueOf(fileExtension) == type) {
                return MIME_TYPE.valueOf(fileExtension).getContentTypeString();
            }
        }
        return "text/plain";
    }
}
