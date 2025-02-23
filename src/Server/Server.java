package Server;

import Methods.Server.ServerMethods;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server class initializes a server that can handle multiple client connections
 * simultaneously using a thread pool.
 */
public class Server {

    /**
     * Main method to start the server and handle client connections.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        int port = 6666;
        // Creates a thread pool with 10 threads
        ExecutorService executor = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started, waiting for connections");

            while (true) {
                // Accepts a client connection
                Socket socket = serverSocket.accept();
                System.out.println("Connection received");

                // Checks if the connection is active
                if (ServerMethods.isConnectionActive(socket)) {
                    System.out.println("Connection is active.");
                    // Submits a new task to handle the client connection
                    executor.submit(() -> {
                        try {
                            ServerMethods.handleClient(socket);
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error while handling client: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.out.println("Connection is not active.");
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
