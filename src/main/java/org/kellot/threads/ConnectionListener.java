package org.kellot.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private int port;

    public ConnectionListener(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            int count = 0;
            while (!serverSocket.isClosed()) {
                count++;
                // Listen to TCP connection
                System.out.println("Server is listening on port : " + port);
                clientSocket = serverSocket.accept();
                System.out.println("Client connection count : " + count);
                System.out.println("*** New Connection from : " + clientSocket.getInetAddress());
                // Process the request on another thread
                RequestWorker workerThread = new RequestWorker(clientSocket);
                workerThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
