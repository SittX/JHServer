package org.kellot.dispatcher;

import org.kellot.MIME_TYPE;
import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;
import org.kellot.resource.ResponseData;
import org.kellot.resource.ResourceManager;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseBuilder;
import org.kellot.response.HttpResponseStatus;
import org.kellot.util.HttpDate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Dispatch HTTP response back to the client with requested data or
 * send back error page when the request is not valid.
 *
 * @author SittX
 */
public class RequestDispatcher {
    private final BufferedOutputStream output;
    private final ResourceManager resourceManager;
    private final ServerConfiguration conf;

    public RequestDispatcher(OutputStream output) {
        this.output = new BufferedOutputStream(output);
        this.resourceManager = ResourceManager.getInstance();
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    /**
     * Dispatch response to the client with the targeted path in the request.
     * It'll find the requested path in the "root" directory and return data if it exists in the "root" directory.
     * @param request is the HttpRequest object.
     * @throws IOException when the path is not found in the "root" directory.
     */
    public void dispatchResponse(HttpRequest request) {
        String reqAcceptType = request.getHeaders().get("Accept");
        String reqContentType = reqAcceptType != null ?  reqAcceptType.split("/")[0]:null;
        String requestPath = request.getPath();
        String fileExtension = requestPath.substring(requestPath.lastIndexOf('.') + 1).toUpperCase();
        String contentType = getContentTypeForFileExtension(fileExtension);
        String filePath = conf.pageLocation() + requestPath;

        switch(reqContentType){
            case "image":
                BufferedImage image =resourceManager.fetchImageData(filePath);
                try {
                    ImageIO.write(image,fileExtension,output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            default:
                ResponseData responseData = resourceManager.fetchResourceData(filePath);
                if (responseData.isValid()) {
                    byte[] resultData = responseData.getData();
                    HttpResponse response = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                            .setBody(new String(resultData))
                            .setHeader("Connection", "keep-alive")
                            .setHeader("Content-Type", contentType)
                            .setHeader("Content-Length", String.valueOf(resultData.length))
                            .setHeader("Date", HttpDate.setCurrentDateTime())
                            .setHeader("Expires", HttpDate.setExpiryDateTime(1))
                            .setHeader("Server", "Kev JHServer version 1")
                            .build();
                    sendResponse(response);
                }

        }

//        ResponseData responseData = resourceManager.fetchResourceData();

//        String contentType = getContentTypeForFileExtension(fileExtension);
//        if (responseData.isValid()) {
//            byte[] resultData = responseData.getData();
//            HttpResponse response = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
//                    .setBody(new String(resultData))
//                    .setHeader("Connection","keep-alive")
//                    .setHeader("Content-Type",contentType)
//                    .setHeader("Content-Length",String.valueOf(resultData.length))
//                    .setHeader("Date",HttpDate.setCurrentDateTime())
//                    .setHeader("Expires",HttpDate.setExpiryDateTime(1))
//                    .setHeader("Server","Kev JHServer version 1")
//                    .build();
//            sendResponse(response);
//        } else {
//            dispatchError(HttpResponseStatus.NOT_FOUND);
//        }
    }

    /**
     * Dispatch error page to the client
     * @param responseStatus is the status of the HttpResponse
     */
    public void dispatchError(HttpResponseStatus responseStatus) {
        ResponseData responseData = resourceManager.fetchResourceData(conf.errorTemplateLocation());

        if (!responseData.isValid()) {
            throw new RuntimeException("Error template cannot be found. Please check the template location in the configuration file.");
        }

        byte[] errorTemplate = responseData.getData();
        HttpResponse response = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                .setBody(new String(errorTemplate))
                .setHeader("Connection","keep-alive")
                .setHeader("Content-Type","text/html")
                .setHeader("Content-Length",String.valueOf(errorTemplate.length))
                .setHeader("Server","Kev JHServer version 1")
                .setHeader("Date",HttpDate.setCurrentDateTime())
                .setHeader("Expires",HttpDate.setExpiryDateTime(1))
                .build();
        sendResponse(response);
    }

    private String getContentTypeForFileExtension(String fileExtension) {
        for (MIME_TYPE type : MIME_TYPE.values()) {
            if (MIME_TYPE.valueOf(fileExtension) == type) {
                return MIME_TYPE.valueOf(fileExtension).getContentTypeString();
            }
        }
        return "text/html";
    }

    /**
     * Receive HttpResponse object and sent it back along with requested page to the OutputStream.
     * @param response is HttpResponse object
     */
    private void sendResponse(HttpResponse response) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(response.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data into the OutputStream. " + e.toString());
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                throw new RuntimeException("Error when closing the OutputStream. " + e.getMessage());
            }
        }
    }



}
