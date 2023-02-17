package org.kellot;

import org.kellot.config.Configuration;
import org.kellot.config.ConfigurationManager;
import org.kellot.threads.RequestListenerThread;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        ConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        Logger logger = Logger.getLogger(Main.class.getName());
        logger.info("Port Number -> " + conf.getPort());
        logger.info("Root Path -> " + conf.getRoot());

        RequestListenerThread requestListener = null;

        try {
            requestListener = new RequestListenerThread(conf.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        requestListener.start();
    }
}