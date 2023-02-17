package org.kellot;

import org.junit.jupiter.api.Test;
import org.kellot.config.Configuration;
import org.kellot.config.ConfigurationManager;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {

    @Test
    void searchPage() {
        ConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        ResourceManager resourceManager = ResourceManager.getInstance();
        boolean result = resourceManager.searchPage("hello.html");

        try {
            byte[] fileContent = resourceManager.getPage("hello.html");
            for (byte b : fileContent) {
                System.out.println(b);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        assertTrue(result);
    }

    @Test
    void getPage() {
    }
}