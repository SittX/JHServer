package org.kellot;

import org.kellot.config.Configuration;
import org.kellot.config.ConfigurationManager;
import org.kellot.threads.ConnectionListener;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ConfigurationManager.getInstance().initializeConfiguration("src/main/resources/JHConfig.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        System.out.println("Port Number : " + conf.getPort());
        System.out.println("Root Path : " + conf.getRoot());

        ConnectionListener connectionListenerThread = null;
        try {
            connectionListenerThread = new ConnectionListener(conf.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connectionListenerThread.start();
    }
}