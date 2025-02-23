package Client;

import Methods.Client.ClientMessage;

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

        // Loop to allow the user to continue sending messages until they decide to exit
        while (true) {
            System.out.println("Enter your nickname:");
            String nickname = sc.nextLine();
            System.out.println("Type the message you want to send:");
            String message = sc.nextLine();

            // Create a new thread to handle each client connection
            new Thread(() -> {
                try (Socket socket = new Socket(host, port);
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

                    // Create a client message with the entered nickname and message
                    ClientMessage clientMessage = new ClientMessage(nickname, message);
                    //System.out.println("Message sent to server from " + nickname);
                    // Send the message to the server
                    output.writeObject(clientMessage);
                } catch (Exception e) {
                    System.out.println("Error occurred while sending message from " + nickname + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }).start(); // Start the new thread

            // Ask if the user wants to send another message
            System.out.println("Do you want to send another message? (Y/N)");
            if (!sc.nextLine().equalsIgnoreCase("Y")) {
                break; // If the answer is not 'Y', exit the loop
            }

        }
    }
}
