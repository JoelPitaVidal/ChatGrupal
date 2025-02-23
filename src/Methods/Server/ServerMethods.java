package Methods.Server;

import Methods.Client.ClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerMethods {

    /**
     * Handles the communication with a single client.
     *
     * @param socket The client socket.
     * @throws IOException If an I/O error occurs when accessing the socket.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static void handleClient(Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("Handling client connection..."); // Debug message for handling client connection
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // Reads data received from the client
            ClientMessage clientMessage = (ClientMessage) input.readObject();
            System.out.println("Nickname: " + clientMessage.getNickname());
            System.out.println("Message: " + clientMessage.getMessage());
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage()); // Debug message for socket exceptions
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage()); // Debug message for IO exceptions
            e.printStackTrace();
        } finally {
            // Closes the client socket
            socket.close();
            System.out.println("Client connection closed."); // Debug message for closing client connection
        }
    }

    /**
     * Checks if the client connection is still active.
     *
     * @param socket The client socket.
     * @return true if the connection is active, false otherwise.
     */
    public static boolean isConnectionActive(Socket socket) {
        return !socket.isClosed() && socket.isConnected();
    }
}
