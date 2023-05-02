package org.kellot.resource;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.request.HttpRequest;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Objects;

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

    public ResourceData getResourceData(HttpRequest request,BufferedOutputStream output) {
        System.out.println(request.getPath()+" : "+ request.getHeaders().get("Accept"));
        ResourceData data = new ResourceData();

        String requestPath = request.getPath();
        String accept = request.getHeaders().get("Accept");
        String requestAcceptType = accept.split("/")[0];
        String fileExtension = requestPath.substring(requestPath.lastIndexOf('.') + 1);
        File file = new File(configuration.pageLocation() + requestPath);

        if(Objects.equals(requestAcceptType, "image") || fileExtension.equals("jpg") || fileExtension.equals("png")){
            try {
                ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                Image image = imageIcon.getImage();
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
                Graphics graphics = bufferedImage.getGraphics();
                graphics.drawImage(image,0,0,null);
                graphics.dispose();

                ImageIO.write(bufferedImage,fileExtension,output);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                data.setData(fileInputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
