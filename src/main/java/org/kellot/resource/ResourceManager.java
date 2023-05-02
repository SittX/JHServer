package org.kellot.resource;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    // TODO when data is sent back to client, it should be sent through Compressing stream for better efficiency
    // TODO Implement different type of reading for different file extensions (e.g: images should use ImageBuffer and File should use FileInputStream)
    public ResourceData getResourceData(String resourcePath) {
        File file = new File(resourcePath);
        ResourceData data = new ResourceData();
        String fileExtension = resourcePath.substring(resourcePath.lastIndexOf('.') + 1);

        if(fileExtension == "jpg" || fileExtension == "png"){
            System.out.println("Request is an Image");
        }

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                data.setData(fileInputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return data;
    }
}
