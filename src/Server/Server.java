package Server;

import Methods.Client.ClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

                // Submits a new task to handle the client connection
                executor.submit(() -> {
                    try {
                        handleClient(socket);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the communication with a single client.
     *
     * @param socket The client socket.
     * @throws IOException If an I/O error occurs when accessing the socket.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private static void handleClient(Socket socket) throws IOException, ClassNotFoundException {
        // Initializes input and output streams for object transmission
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

        // Reads data received from the client
        ClientMessage clientMessage = (ClientMessage) input.readObject();
        System.out.println("Nickname: " + clientMessage.getNickname());
        System.out.println("Message: " + clientMessage.getMessage());

        // Closes the client socket
        socket.close();
    }
}
