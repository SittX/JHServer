package org.kellot.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * A worker thread that listens to and keep tracks of the incoming message
 * It also creates a TCP socket for individual connection.
 *
 * @author SittX
 */

public class RequestListenerThread extends Thread {
    private final static Logger logger = Logger.getLogger(RequestListenerThread.class.getName());
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;

    /**
     * @param port is the port number that the server has to listen for the incoming requests.
     * @throws IOException
     */
    public RequestListenerThread(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Accept incoming requests and create a TCP socket for each request.
     * It also keeps track of how many requests has been made to the server and
     * log the information to the console.
     */
    @Override
    public void run() {
        try {
            int connectionCounter = 0;
            while (!serverSocket.isClosed()) {
                connectionCounter++;
                // Listen to TCP connection
                logger.info("Server is listening on port -> " + port);
                clientSocket = serverSocket.accept();
                logger.info("Total connection count -> " + connectionCounter);
                logger.info("*** A new connection is established from -> " + clientSocket.getInetAddress());
                // Spawn another thread to process the request
                RequestWorkerThread workerThread = new RequestWorkerThread(clientSocket);
                workerThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
