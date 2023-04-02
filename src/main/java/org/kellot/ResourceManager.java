package org.kellot;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManagerImpl;

import java.io.File;
import java.io.FileInputStream;

public class ResourceManager {
    private static ResourceManager resourceManager;
    private ServerConfiguration configuration;

    private ResourceManager() {
        configuration = ServerConfigurationManagerImpl.getInstance().getCurrentConfiguration();
    }

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    // TODO when data is sent back to client, it should be sent through Compressing stream for better efficiency
    public ResourceData getResourceData(String resourcePath) {
        File file = new File(resourcePath);

        ResourceData data = new ResourceData();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
           data.setData(fileInputStream.readAllBytes());
        } catch (Exception e) {
            // Do not handle the exception. Log it.
            e.printStackTrace();
        }

        return data;
    }
}
