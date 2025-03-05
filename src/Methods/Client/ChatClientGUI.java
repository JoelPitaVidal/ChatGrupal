package Methods.Client;

import Methods.Client.ClientMessage;
import Methods.JsonFiles.JsonFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

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

        // Load previous messages
        loadPreviousMessages();

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                //Help command
                if (message.equalsIgnoreCase("/help")) {
                    chatArea.append("/bye = exit chat\n/AFK = stay AFK\n/bussy = you are doing something");
                }
                //Command to exit the chat
                if (message.equalsIgnoreCase("/bye")) {
                    chatArea.append("the user "+nickname+"has been disconnected");
                    System.exit(0);
                }
                //Command to stay AFK
                if (message.equalsIgnoreCase("/AFK")) {
                    chatArea.append("I will come back soon");

                }
                //Command to be busy
                if (message.equalsIgnoreCase("/busy")) {
                    chatArea.append("I`m busy, don`t disturb");
                }
                // Add the message to the ClientMessage object
                clientMessage.addMessage(nickname, message);

                // Send the message to the server
                output.writeObject(clientMessage);

                // Clear the message field
                messageField.setText("");

                // Display the message in the chat area
                chatArea.append("Me: " + message + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void loadPreviousMessages() {
        chatArea.append("COMMANDS:\n/help to see the available commands");

        // Load all messages from all users
        java.util.List<ClientMessage> clientMessages = JsonFiles.loadMessages();
        for (ClientMessage cm : clientMessages) {
            for (Message msg : cm.getMessages()) {
                chatArea.append(msg.getUsuario() + ": " + msg.getMensaje() + "\n");
            }
        }
    }
}