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
        Scanner sc = new Scanner(System.in);

        int usercount = 0;

        // Ask for the server IP and port
        System.out.println("Enter the server IP:");
        String host = sc.nextLine();
        System.out.println("Enter the server port:");
        int port = Integer.parseInt(sc.nextLine());

        // Ask for the user's nickname once
        System.out.println("Enter your nickname:");
        String nickname = sc.nextLine();
        System.out.println("The user "+nickname+" has been connected \n");
        usercount ++;
        System.out.println("There are a total of "+usercount+" users online\n");
        System.out.println("/help to see the available commands");
        // Load or create a ClientMessage object for the user
        ClientMessage clientMessage = JsonFiles.loadOrCreateClientMessage(nickname);

        // Display previous messages for this nickname, if any
        // System.out.println("Loaded messages for " + nickname + ":");
        //System.out.println(clientMessage);

        // Display all messages from all users
        // System.out.println("All messages from all users:");
        JsonFiles.printMessages();

        // Attempt to connect to the server
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            if (isConnectionEstablished(socket)) {
                // System.out.println("Successfully connected to the server.");

                // Loop to allow the user to continue sending messages until they decide to exit
                while (true) {
                    //System.out.println("Write your message:");
                    String message = sc.nextLine();

                    // Check if the user wants to exit the chat
                    if (message.equalsIgnoreCase("/bye")) {
                        System.out.println("The user "+nickname+"has been disconnected");
                        break; // Exit the loop if the user types /exitChat
                    }

                    // Comand to remain AFK
                    if (message.equalsIgnoreCase("/help")) {
                        System.out.println("commands:" +
                                "\n/bye = exit chat\n/Hello = say hello\n/AFK = remain AFK\n/busy = you are bussy with something");
                    }


                    // Comand to say hello
                    if (message.equalsIgnoreCase("/Hello")) {
                        System.out.println("Hello There");
                    }

                    // Comand to remain AFK
                    if (message.equalsIgnoreCase("/AFK")) {
                        System.out.println("I'll be back soon");
                    }

                    // Comand to remain AFK
                    if (message.equalsIgnoreCase("/busy")) {
                        System.out.println("I'll be back in 5 minutes");
                    }


                    // Add the message to the ClientMessage object
                    clientMessage.addMessage(nickname, message);

                    // Send the message to the server
                    output.writeObject(clientMessage);
                    // System.out.println("Message sent to server from " + nickname);

                    // Read the acknowledgment from the server
                    String ack = (String) input.readObject();
                    System.out.println(ack);

                    // Clear the terminal
                    clearTerminal();

                    // Display all messages from all users
                    // JsonFiles.printMessages();
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