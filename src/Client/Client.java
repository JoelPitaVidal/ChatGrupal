package Client;

import Methods.Client.ClientMessage;

import java.io.ObjectOutputStream;
import java.net.Socket;

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

        // Data to send to the server
        ClientMessage clientMessage1 = new ClientMessage("User1", "Hello from User1");
        ClientMessage clientMessage2 = new ClientMessage("User2", "Hello from User2");

        // Send first message
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            // Sends data to the server
            output.writeObject(clientMessage1);
            System.out.println("Data sent to server from User1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send second message
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            // Sends data to the server
            output.writeObject(clientMessage2);
            System.out.println("Data sent to server from User2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
