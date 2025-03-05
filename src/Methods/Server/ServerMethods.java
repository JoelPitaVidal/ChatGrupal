package Methods.Server;

import Methods.Client.ClientMessage;
import Methods.Client.Message;
import Methods.JsonFiles.JsonFiles;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMethods {
    private static List<ObjectOutputStream> clientOutputs = new ArrayList<>();
    private static int userCount = 0;

    /**
     * Handles the communication with a single client.
     *
     * @param socket The client socket.
     * @throws IOException If an I/O error occurs when accessing the socket.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static void handleClient(Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("Handling client connection...");
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            synchronized (clientOutputs) {
                clientOutputs.add(output);
                userCount++;
            }

            boolean continueChat = true;
            while (continueChat && !socket.isClosed()) {
                // Reads data received from the client
                ClientMessage clientMessage = (ClientMessage) input.readObject();
                Message lastMessage = clientMessage.getMessages().get(clientMessage.getMessages().size() - 1);
                System.out.println("Nickname: " + clientMessage.getNickname());
                System.out.println("Message: " + lastMessage);

                // Add the message to the JSON file
                JsonFiles.addMessage(clientMessage.getNickname(), lastMessage.getUsuario(), lastMessage.getMensaje());

                // Broadcast the message to all clients
                broadcastMessage(lastMessage.getUsuario() + ": " + lastMessage.getMensaje());

                // Checks if the client wants to continue
                if (lastMessage.getMensaje().equalsIgnoreCase("has disconnected.")) {
                    continueChat = false;
                    synchronized (clientOutputs) {
                        clientOutputs.remove(output);
                        userCount--;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (!socket.isClosed()) {
                socket.close();
                System.out.println("Connection closed");
            }
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to broadcast.
     */
    private static void broadcastMessage(String message) {
        synchronized (clientOutputs) {
            for (ObjectOutputStream output : clientOutputs) {
                try {
                    output.writeObject(message + " (Total users: " + userCount + ")");
                } catch (IOException e) {
                    System.out.println("Error broadcasting message: " + e.getMessage());
                    e.printStackTrace();
                }
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