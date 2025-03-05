package Methods.Client;

import Methods.Client.ClientMessage;
import Methods.JsonFiles.JsonFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private ClientMessage clientMessage;
    private String nickname;
    private ObjectOutputStream output;

    public ChatClientGUI(String nickname, ClientMessage clientMessage, ObjectOutputStream output) {
        this.nickname = nickname;
        this.clientMessage = clientMessage;
        this.output = output;

        setTitle("Chat Client - " + nickname);
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                if (message.equalsIgnoreCase("/exitChat")) {
                    System.exit(0);
                }

                // Add the message to the ClientMessage object
                clientMessage.addMessage(nickname, message);

                // Send the message to the server
                output.writeObject(clientMessage);

                // Clear the message field
                messageField.setText("");

                // Display the message in the chat area
                chatArea.append("Me: " + message + "\n");

                // Clear the terminal
                clearTerminal();

                // Display all messages from all users
                JsonFiles.printMessages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    public static void clearTerminal() {
        // This sequence works on most ANSI-compatible terminals
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}