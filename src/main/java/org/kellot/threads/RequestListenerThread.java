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
    private final ServerSocket serverSocket;
    private final int port;
    private int connectionCounter = 0;

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
        logger.info("Server is listening on port -> " + port);

        while (serverSocket.isBound() && !serverSocket.isClosed()) {

            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            logger.info("Total connection count -> " + ++connectionCounter);
            logger.info(clientSocket.getInetAddress() + " is connected to the server.");

            // Start RequestProcessingThread to process the incoming request
            RequestProcessingThread workerThread = new RequestProcessingThread(clientSocket);
            workerThread.start();
        }
    }
}
