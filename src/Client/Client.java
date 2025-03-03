package Client;

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
        String host = "localhost";
        int port = 6666;
        Scanner sc = new Scanner(System.in);

        // Ask for the user's nickname once
        System.out.println("Enter your nickname:");
        String nickname = sc.nextLine();

        // Load or create a ClientMessage object for the user
        ClientMessage clientMessage = JsonFiles.loadOrCreateClientMessage(nickname);

        // Display previous messages for this nickname, if any
        // System.out.println("Loaded messages for " + nickname + ":");
        // System.out.println(clientMessage);

        // Display all messages from all users
        // System.out.println("All messages from all users:");
        JsonFiles.printMessages();

        // Attempt to connect to the server
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            if (isConnectionEstablished(socket)) {
                System.out.println("Successfully connected to the server.");

                // Loop to allow the user to continue sending messages until they decide to exit
                while (true) {
                    System.out.println("Write your message:");
                    String message = sc.nextLine();

                    // Add the message to the ClientMessage object
                    clientMessage.addMessage(nickname, message);

                    // Send the message to the server
                    output.writeObject(clientMessage);
                    // System.out.println("Message sent to server from " + nickname);

                    // Read the acknowledgment from the server
                    // String ack = (String) input.readObject();
                    // System.out.println("Server response: " + ack);

                    // Ask if the user wants to send another message
                    System.out.println("Do you want to send another message? (Y/N)");
                    if (!sc.nextLine().equalsIgnoreCase("Y")) {
                        clearTerminal();
                        JsonFiles.printMessages();
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


    /**
     * Clears the terminal screen.
     */
    public static void clearTerminal() {
        // This sequence works on most ANSI-compatible terminals
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}