package org.kellot.dispatcher;

import org.kellot.ResourceManager;
import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;
import org.kellot.response.HttpResponse;
import org.kellot.response.HttpResponseBuilder;
import org.kellot.response.HttpResponseStatus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Dispatch HTTP response back to the client with requested data or
 * send back error page when the request is not valid.
 * @author SittX
 */
public class Dispatcher {
    private final OutputStreamWriter output;
    private final ResourceManager resourceManager;
    private final ServerConfiguration conf;

    public Dispatcher(OutputStreamWriter output) {
        this.output = output;
        this.resourceManager = ResourceManager.getInstance();
        this.conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    /**
     * Dispatch response to the client with the targeted path in the request.
     * It'll find the requested path in the "root" directory and return data if it exists in the "root" directory.
     * @param request is the HttpRequest object.
     * @throws IOException when the path is not found in the "root" directory.
     */
    public void dispatchResponse(HttpRequest request){
        String path = request.getPath();
        byte[] htmlCode = resourceManager.readFileIntoByteArray(conf.getPageLocation() + path);
        if (htmlCode.length == 0) {
            HttpResponse response = new HttpResponseBuilder(HttpResponseStatus.OK, HttpResponseStatus.OK.getCode())
                    .setBody(new String(htmlCode))
                    .setHeaders(Map.ofEntries(
                            Map.entry("Content-Type", "text/html"),
                            Map.entry("Content-Length", String.valueOf(htmlCode.length)),
                            Map.entry("Date", getHttpDate()),
                            Map.entry("Expires", getExpiryDate(1))
                    ))
                    .build();
            sendResponse(response);
        } else {
            dispatchError(HttpResponseStatus.NOT_FOUND);
        }
    }

    /**
     * Receive HttpResponse object and sent it back along with requested page to the OutputStream.
     * @param response is HttpResponse object
     */
    private void sendResponse(HttpResponse response) {
        try {
            output.write(response.toString());
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data into the OutputStream. " + e.getMessage());
        }

        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("Error when closing the OutputStream. " + e.getMessage());
        }
    }

    /**
     * Dispatch error page to the client
     * @param responseStatus is the status of the HttpResponse
     */
    public void dispatchError(HttpResponseStatus responseStatus) {
        byte[] htmlCode = resourceManager.readFileIntoByteArray(conf.getErrorTemplateLocation());

        HttpResponse response = new HttpResponseBuilder(responseStatus, responseStatus.getCode())
                .setBody(new String(htmlCode))
                .setHeaders(Map.ofEntries(
                        Map.entry("Content-Type", "text/html"),
                        Map.entry("Content-Length", String.valueOf(htmlCode.length)),
                        Map.entry("Date", getHttpDate()),
                        Map.entry("Expires", getExpiryDate(1))
                ))
                .build();
        sendResponse(response);
    }

    /**
     * Get the current date and time in the UTC timezone and format it in the HTTP "Date" header format.
     * Format -> Fri, 31 Dec 1999 23:59:59 GMT
     * @return String of "Fri, 31 Dec 1999 23:59:59 GMT"
     */
    private String getHttpDate() {
        DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        return httpDateFormat.format(currentDateTime);
    }

    /**
     * Same as getHttpDate() method but it adds some additional hours for expiry date.
     * @param expiryHour
     * @return String of "Fri, 31 Dec 1999 23:59:59 GMT"
     */
    private String getExpiryDate(long expiryHour) {
        DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC).plusHours(expiryHour);
        return httpDateFormat.format(currentDateTime);
    }
}
