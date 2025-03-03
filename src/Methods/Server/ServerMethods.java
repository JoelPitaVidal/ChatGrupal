package Methods.Server;

import Methods.Client.ClientMessage;
import Methods.Client.Message;

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

            boolean continueChat = true;
            while (continueChat && !socket.isClosed()) {
                // Reads data received from the client
                ClientMessage clientMessage = (ClientMessage) input.readObject();
                Message lastMessage = clientMessage.getMessages().get(clientMessage.getMessages().size() - 1);
                System.out.println("Nickname: " + clientMessage.getNickname());
                System.out.println("Message: " + lastMessage);

                // Sends acknowledgment to client
                output.writeObject("Message received: " + lastMessage);

                // Checks if the client wants to continue
                if (lastMessage.getMensaje().equalsIgnoreCase("N")) {
                    continueChat = false;
                }
            }
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage()); // Debug message for socket exceptions
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage()); // Debug message for IO exceptions
            e.printStackTrace();
        } finally {
            // Closes the client socket
            if (!socket.isClosed()) {
                socket.close();
                System.out.println("Connection Closed"); // Message when connection is closed
            }
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