package org.kellot.resource;

import org.kellot.config.ServerConfiguration;
import org.kellot.config.ServerConfigurationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceManager {
    private static ResourceManager resourceManager;
    private final ServerConfiguration configuration;

    public ResourceManager() {
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
    public ResponseData fetchFileData(String filePath){
        ResponseData data = new ResponseData();
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                data.setData(fileInputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return data;
    }


    public BufferedImage fetchImageData(String imageFilePath) {
            ImageIcon imageIcon = new ImageIcon(imageFilePath);
            Image image = imageIcon.getImage();
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            return  bufferedImage;
    }


//    public ResponseData fetchIcoImage(String icoImagePath) {
//        try {
//            List<BufferedImage> images = ICODecoder.read(new File(icoImagePath));
//
//            if (images.isEmpty()) {
//                throw new IllegalArgumentException("No images found in the ICO file: " + icoImagePath);
//            }
//
//            BufferedImage firstImage = images.get(0);
//            Image image = firstImage.getScaledInstance(firstImage.getWidth(), firstImage.getHeight(), Image.SCALE_DEFAULT);
//            BufferedImage combinedImage = new BufferedImage(firstImage.getWidth(), firstImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//            Graphics2D g = combinedImage.createGraphics();
//            g.drawImage(image, 0, 0, null);
//            g.dispose();
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ImageIO.write(combinedImage, "ico", byteArrayOutputStream);
//            byte[] imageInByte = byteArrayOutputStream.toByteArray();
//
//            ResponseData responseData = new ResponseData();
//            responseData.setData(imageInByte);
//            return responseData;
//
//        } catch (IOException e) {
//            throw new RuntimeException("Error occurred while reading ICO file: " + icoImagePath, e);
//        }
//    }


    public ResponseData anotherFetchImageData(String filePath) {
        ResponseData responseData = new ResponseData();
        String fileExtension = filePath.substring(filePath.lastIndexOf('.') + 1);
        try {
            File file = new File(filePath);
            BufferedImage originalImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, fileExtension, baos);
            byte[] imageInByte = baos.toByteArray();
            responseData.setData(imageInByte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseData;
    }

}
