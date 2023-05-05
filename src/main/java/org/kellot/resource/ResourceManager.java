package org.kellot.resource;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ResourceManager {
    private static ResourceManager resourceManager;
    private final ServerConfiguration configuration;

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
    // TODO Refactor this method. Why should ResourceManager know about the request content-type ?
    //  The resource manager main responsibility is to fetch resources into byte[] and return to the caller.
    public ResponseData fetchResourceData(String resourcePath) {
        ResponseData data = new ResponseData();
            try (FileInputStream fileInputStream = new FileInputStream(resourcePath)) {
                data.setData(fileInputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return data;
    }

    public BufferedImage fetchImageData(String imageFilePath){
        ImageIcon imageIcon = new ImageIcon(imageFilePath);
        Image image = imageIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return  bufferedImage;
    }

}
