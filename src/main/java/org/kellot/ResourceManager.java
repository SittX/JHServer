package org.kellot;

import org.kellot.config.Configuration;
import org.kellot.config.ConfigurationManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ResourceManager {
    private static ResourceManager resourceManager;
    private static Configuration conf;

    private ResourceManager() {
        conf = ConfigurationManager.getInstance().getCurrentConfiguration();
    }

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    private static byte[] readContentIntoByteArray(File file) {
        byte[] fileBuffer = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBuffer;
    }

    public boolean searchPage(String fileName) {
        File file = new File(conf.getPages() + fileName);
        return file.exists();
    }

    // TODO when data is sent back to client, it should be sent through Compressing stream for better efficiency
    public byte[] getPage(String fileName) throws FileNotFoundException {
        File file = new File(conf.getPages() + fileName);

        if (!searchPage(fileName)) {
            throw new FileNotFoundException(file.getName() + " is not found in " + conf.getPages());
        }

        return readContentIntoByteArray(file);
    }
}
