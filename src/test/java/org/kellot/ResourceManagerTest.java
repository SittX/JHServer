package org.kellot;

import org.junit.jupiter.api.Test;
import org.kellot.config.ServerConfigurationManager;
import org.kellot.response.HttpResponseStatus;

import java.io.FileNotFoundException;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceManagerTest {

    @Test
    void searchPage() {
        ServerConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        ResourceManager resourceManager = ResourceManager.getInstance();

            byte[] fileContent = resourceManager.readFileIntoByteArray("hello.html");
            for (byte b : fileContent) {
                System.out.println(b);
            }

    }

    @Test
    void testEnum() {
        HttpResponseStatus status = HttpResponseStatus.valueOf("OK");
        if (status == HttpResponseStatus.OK) {
            System.out.println("Value is OK.");
        }

        System.out.println("Status : " + HttpResponseStatus.OK.getCode());
        System.out.println("Code : " + HttpResponseStatus.OK);

        System.out.println("\n\n");
        for (HttpResponseStatus s : HttpResponseStatus.values()) {
            System.out.println("Status : " + s);
            System.out.println("Code : " + s.getCode());
        }
    }

    @Test
    void testDate() {
        for (String zone : ZoneId.getAvailableZoneIds()) {
            if (zone.contains("Rangoon")) {
                System.out.println(zone);
            }
        }
    }

    @Test
    void getPage() {
    }
}