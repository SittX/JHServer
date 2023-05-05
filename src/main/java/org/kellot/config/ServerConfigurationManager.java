package org.kellot.config;

import org.kellot.util.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

/*
    ConfigurationManager is a singleton class for loading, and managing configuration information for the HTTP server.
 */
public class ServerConfigurationManager {
    private static ServerConfigurationManager configurationManager;
    private ServerConfiguration configuration;

    private ServerConfigurationManager() {
    }

    public static ServerConfigurationManager getInstance() {
        if (configurationManager == null) {
            configurationManager = new ServerConfigurationManager();
        }
        return configurationManager;
    }

    public void initializeConfiguration(String filePath) {
        try {
            configuration = JsonParser.toObject(new FileReader(filePath), ServerConfiguration.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error : Server configuration file cannot be found -> " + e);
        }
    }

    public ServerConfiguration getCurrentConfiguration() {
        if (configuration == null) {
            throw new RuntimeException("Error : Configuration object has not been initialized.");
        }
        return configuration;
    }
}
