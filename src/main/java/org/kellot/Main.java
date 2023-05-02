package org.kellot;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.threads.RequestListenerThread;

import java.io.IOException;

/**
 * Entry class for the server which setup configuration and start the listener thread
 *
 * @author SittX
 */
public class Main {
    /**
     * Set up the configuration for the server and start listening the incoming requests on a different thread.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        ServerConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        ServerConfiguration conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();

        // Don't handle the exception here because we can't do anything about TCP connection IO exception.
        RequestListenerThread requestListener = new RequestListenerThread(conf.port());
        requestListener.start();
    }
}
