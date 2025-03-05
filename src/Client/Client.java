package Client;

import Methods.Client.ChatClientGUI;
import Methods.Client.ClientMessage;
import Methods.JsonFiles.JsonFiles;

import java.io.ObjectInputStream;
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
        Scanner sc = new Scanner(System.in);

        // Ask for the server IP and port
        System.out.println("Enter the server IP:");
        String host = sc.nextLine();
        System.out.println("Enter the server port:");
        int port = Integer.parseInt(sc.nextLine());

        // Ask for the user's nickname once
        System.out.println("Enter your nickname:");
        String nickname = sc.nextLine();

        // Load or create a ClientMessage object for the user
        ClientMessage clientMessage = JsonFiles.loadOrCreateClientMessage(nickname);

        // Attempt to connect to the server
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            if (isConnectionEstablished(socket)) {
                System.out.println("Successfully connected to the server.");

                // Create and display the chat GUI
                ChatClientGUI chatClientGUI = new ChatClientGUI(nickname, clientMessage, output);

                // Send a connection message to the server
                clientMessage.addMessage(nickname, "has connected.");
                output.writeObject(clientMessage);

                // Loop to allow the user to continue sending messages until they decide to exit
                while (true) {
                    // Read the acknowledgment from the server
                    String ack = (String) input.readObject();
                    chatClientGUI.displayMessage(ack);

                    // Read the user's message
                    String message = sc.nextLine();

                    // Check if the user wants to exit the chat
                    if (message.equalsIgnoreCase("/exitChat")) {
                        clientMessage.addMessage(nickname, "has disconnected.");
                        output.writeObject(clientMessage);
                        break; // Exit the loop if the user types /exitChat
                    }

                    // Add the message to the ClientMessage object
                    clientMessage.addMessage(nickname, message);

                    // Send the message to the server
                    output.writeObject(clientMessage);
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