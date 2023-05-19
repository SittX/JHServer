package org.kellot.dispatcher;

import org.kellot.MIME_TYPE;
import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;
import org.kellot.request_handlers.DeleteRequestHandler;
import org.kellot.request_handlers.GetRequestHandler;
import org.kellot.request_handlers.PostRequestHandler;
import org.kellot.resource.ResourceManager;
import org.kellot.resource.ResponseData;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseBuilder;
import org.kellot.response.HttpResponseStatus;
import org.kellot.util.HttpDate;
import org.mockito.internal.util.io.IOUtil;

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
    private final HttpRequest request;

    public RequestDispatcher(HttpRequest request ,OutputStream output) {
        this.request = request;
        this.output = new BufferedOutputStream(output);
        this.resourceManager = ResourceManager.getInstance();
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    /**
     * Dispatch response to the client with the targeted path in the request.
     * It'll find the requested path in the "root" directory and return data if it exists in the "root" directory.
     * @throws IOException when the path is not found in the "root" directory.
     */
    public HttpResponse dispatchRequest() {
        String httpMethod = request.getMethod().toUpperCase();
        return switch(httpMethod){
            case "POST"->new PostRequestHandler(request).handleRequest();
            case "DELETE"-> new DeleteRequestHandler(request).handleRequest();
            default-> new GetRequestHandler(request).handleRequest();
        };
    }

    /**
     * Dispatch error page to the client
     * @param responseStatus is the status of the HttpResponse
     */
    public void dispatchError(HttpResponseStatus responseStatus) {
        ResponseData responseData = resourceManager.fetchFileData(conf.errorTemplateLocation());

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
            IOUtil.closeQuietly(output);
        }
    }



}
