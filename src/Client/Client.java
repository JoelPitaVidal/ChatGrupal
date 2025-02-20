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

        // Data to send to the server
        ClientMessage clientMessage1 = new ClientMessage("User1", "Hello from User1");
        ClientMessage clientMessage2 = new ClientMessage("User2", "Hello from User2");

        // Send first message
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            System.out.println("Introdúzca su nicname");
            clientMessage1.setMessage(sc.nextLine());
            System.out.println("Escriba el mensaje que desea enviar");
            clientMessage1.setMessage(sc.nextLine());
            // Sends data to the server
            output.writeObject(clientMessage1);
            System.out.println("Data sent to server from User1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send second message
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            System.out.println("Introdúzca su nicname");
            clientMessage2.setMessage(sc.nextLine());
            System.out.println("Escriba el mensaje que desea enviar");
            clientMessage2.setMessage(sc.nextLine());
            // Sends data to the server
            output.writeObject(clientMessage2);
            System.out.println("Data sent to server from User2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
