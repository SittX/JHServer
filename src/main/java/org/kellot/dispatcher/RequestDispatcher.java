package org.kellot.dispatcher;

import org.kellot.MIME_TYPE;
import org.kellot.resource.ResourceData;
import org.kellot.resource.ResourceManager;
import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseBuilder;
import org.kellot.response.HttpResponseStatus;
import org.kellot.util.HttpDate;

import java.io.*;
import java.util.Map;

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

    public RequestDispatcher(BufferedOutputStream output) {
        this.output = output;
        this.resourceManager = ResourceManager.getInstance();
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    /**
     * Dispatch response to the client with the targeted path in the request.
     * It'll find the requested path in the "root" directory and return data if it exists in the "root" directory.
     *
     * @param request is the HttpRequest object.
     * @throws IOException when the path is not found in the "root" directory.
     */
    public void dispatchResponse(HttpRequest request) {
        String path = request.getPath();
//        ResourceData resourceData = resourceManager.getResourceData(conf.pageLocation() + path);
        ResourceData resourceData = resourceManager.getResourceData(request,output);

//        System.out.println(request.getMethod() +" : "+path);
        if (resourceData.isValid()) {
            String fileExtension = path.substring(path.lastIndexOf('.' ) + 1).toUpperCase();
            String contentType = "";
            for (MIME_TYPE type:MIME_TYPE.values()) {
                if(MIME_TYPE.valueOf(fileExtension) == type ) {
                   contentType = MIME_TYPE.valueOf(fileExtension).getContentTypeString();
                   break;
                }
            }

            byte[] htmlCode = resourceData.getData();
            HttpResponse response = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                    .setBody(new String(htmlCode))
                    .setHeaders(Map.ofEntries(
                            Map.entry("Connection","keep-alive"),
                            Map.entry("Content-Type", contentType),
                            Map.entry("Content-Length", String.valueOf(htmlCode.length)),
                            Map.entry("Date", HttpDate.getCurrentHttpDate()),
                            Map.entry("Expires", HttpDate.getExpiryDate(1))
                    ))
                    .build();
            sendResponse(response);
        } else {
            dispatchError(HttpResponseStatus.NOT_FOUND);
        }
    }

    /**
     * Receive HttpResponse object and sent it back along with requested page to the OutputStream.
     *
     * @param response is HttpResponse object
     */
    private void sendResponse(HttpResponse response) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(response.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data into the OutputStream. " + e.toString());
        }

        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("Error when closing the OutputStream. " + e.getMessage());
        }
    }

    /**
     * Dispatch error page to the client
     *
     * @param responseStatus is the status of the HttpResponse
     */
    public void dispatchError(HttpResponseStatus responseStatus) {
        ResourceData resourceData = resourceManager.getResourceData(conf.errorTemplateLocation());

        if (!resourceData.isValid()) {
            throw new RuntimeException("Resource data cannot be found. Please check the location of the resources.");
        }

        byte[] htmlCode = resourceData.getData();
        HttpResponse response = new HttpResponseBuilder(responseStatus, responseStatus.getCode())
                .setBody(new String(htmlCode))
                .setHeaders(Map.ofEntries(
                        Map.entry("Content-Length", String.valueOf(htmlCode.length)),
                        Map.entry("Date", HttpDate.getCurrentHttpDate()),
                        Map.entry("Expires", HttpDate.getExpiryDate(1)),
                        Map.entry("Connection", "keep-alive"),
                        Map.entry("Server", "KST HTTP server/v1")
                ))
                .build();
        sendResponse(response);
    }

}
