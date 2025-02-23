package Client;

import Methods.Client.ClientMessage;
import Methods.Utils.JsonFiles;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client class connects to the server and sends a ClientMessage object.
 */
public class Client {

    /**
     * Main method to connect to the server and send data.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        String host = "localhost";
        int port = 6666;
        Scanner sc = new Scanner(System.in);

        // Ask for the user's nickname once
        System.out.println("Enter your nickname:");
        String nickname = sc.nextLine();

        // Load messages from JSON file
        String filePath = nickname + "_messages.json";
        ClientMessage clientMessage = JsonFiles.loadMessages(filePath);
        if (clientMessage == null || clientMessage.getNickname().isEmpty()) {
            clientMessage = new ClientMessage(nickname);
        }

        // Attempt to connect to the server
        try (Socket socket = new Socket(host, port)) {
            if (isConnectionEstablished(socket)) {
                System.out.println("Successfully connected to the server.");

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                // Loop to allow the user to continue sending messages until they decide to exit
                while (true) {
                    System.out.println("Type the message you want to send:");
                    String message = sc.nextLine();

                    // Add the message to the ClientMessage object
                    clientMessage.addMessage(message);

                    // Send the message to the server
                    output.writeObject(clientMessage);
                    System.out.println("Message sent to server from " + nickname);

                    // Save the last 10 messages to JSON
                    JsonFiles.saveMessages(clientMessage, filePath);

                    // Ask if the user wants to send another message
                    System.out.println("Do you want to send another message? (Y/N)");
                    if (!sc.nextLine().equalsIgnoreCase("Y")) {
                        break; // If the answer is not 'Y', exit the loop
                    }
                }
            } else {
                System.out.println("Failed to establish connection to the server.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while connecting to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if the connection to the server is established.
     *
     * @param socket The client socket.
     * @return true if the connection is established, false otherwise.
     */
    public static boolean isConnectionEstablished(Socket socket) {
        return socket.isConnected() && !socket.isClosed();
    }
}
