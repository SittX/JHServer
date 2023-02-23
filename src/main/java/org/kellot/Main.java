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
    public static void main(String[] args) {
        ServerConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        ServerConfiguration conf = ServerConfigurationManager.getInstance().getCurrentConfiguration();

        RequestListenerThread requestListener = null;
        try {
            requestListener = new RequestListenerThread(conf.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        requestListener.start();
    }
}