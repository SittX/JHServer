package org.kellot.config;

import org.kellot.util.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

/*
    ConfigurationManager is a singleton class for loading, and managing configuration information for the HTTP server.
 */
public class ConfigurationManager {
    private static ConfigurationManager configurationManager;
    private Configuration configuration;

    private ConfigurationManager() {
    }

    ;

    public static ConfigurationManager getInstance() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public void initializeConfiguration(String filePath) {
        try {
            configuration = JsonParser.toObject(new FileReader(filePath), Configuration.class);
        } catch (FileNotFoundException e) {
            System.out.println("Error : Configuration file cannot be found. " + e);
        }
    }

    public Configuration getCurrentConfiguration() {
        // TODO get the current Configuration object
        // TODO throw exception if the configuration object has not been initialized
        if (configuration == null) {
            throw new RuntimeException("Error : Configuration object has not been initialized.");
        }
        return configuration;

    }
}
