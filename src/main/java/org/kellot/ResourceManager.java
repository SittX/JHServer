package org.kellot;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;

import java.io.File;
import java.io.FileInputStream;

public class ResourceManager {
    private static ResourceManager resourceManager;
    private ServerConfiguration configuration;

    private ResourceManager() {
        configuration = ServerConfigurationManager.getInstance().getCurrentConfiguration();
    }

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    // TODO Completely removed searchPage methods and try to check in the dispatcher
    public boolean searchPage(String fileName) {
        File file = new File(configuration.getPageLocation() + fileName);
        return file.exists();
    }


    // TODO when data is sent back to client, it should be sent through Compressing stream for better efficiency
    public byte[] readFileIntoByteArray(String fileName) {
        File file = new File(fileName);
        byte[] fileBuffer = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBuffer;
    }
}
