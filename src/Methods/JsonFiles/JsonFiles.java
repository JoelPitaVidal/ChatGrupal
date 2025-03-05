package Methods.JsonFiles;

import Methods.Client.ClientMessage;
import Methods.Client.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonFiles {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "json_files/messages.json";
    private static final int MAX_USERS = 10;
    private static final int MAX_MESSAGES = 10;

    /**
     * Save all users' messages to a JSON file
     *
     * @param users The list of ClientMessage objects containing the messages
     */
    public static void saveMessages(List<ClientMessage> users) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(new UsersWrapper(users), writer);
            System.out.println("Messages saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load all users' messages from the JSON file
     *
     * @return A list of ClientMessage objects with all messages
     */
    public static List<ClientMessage> loadMessages() {
        List<ClientMessage> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("File not found: " + FILE_PATH + ". Creating new file...");
            saveMessages(users); // Create an empty file if it doesn't exist
            return users;
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<UsersWrapper>() {}.getType();
            UsersWrapper wrapper = gson.fromJson(reader, type);
            if (wrapper != null) {
                users = wrapper.getUsers();
            }
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error reading file: " + FILE_PATH + " - " + e.getMessage());
        }

        return users;
    }

    /**
     * Load or create a ClientMessage object for a specific nickname.
     * If the user does not exist, it will create one with the nickname and save it.
     *
     * @param nickname The nickname of the client
     * @return The ClientMessage object for the given nickname
     */
    public static ClientMessage loadOrCreateClientMessage(String nickname) {
        List<ClientMessage> users = loadMessages();
        for (ClientMessage user : users) {
            if (user.getNickname().equalsIgnoreCase(nickname)) {
                return user;
            }
        }

        // If the user doesn't exist, create a new ClientMessage
        if (users.size() >= MAX_USERS) {
            System.out.println("Maximum number of users reached. Cannot create new user.");
            return null;
        }

        ClientMessage newUser = new ClientMessage(nickname);
        users.add(newUser);
        saveMessages(users);
        System.out.println("New user created: " + nickname);
        return newUser;
    }

    /**
     * Add a message to a user's message list and save to the JSON file.
     * If the user has more than MAX_MESSAGES, the oldest message will be removed.
     *
     * @param nickname The nickname of the client
     * @param usuario  The user sending the message
     * @param mensaje  The message content
     */
    public static void addMessage(String nickname, String usuario, String mensaje) {
        List<ClientMessage> users = loadMessages();
        ClientMessage user = null;

        for (ClientMessage u : users) {
            if (u.getNickname().equalsIgnoreCase(nickname)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            if (users.size() >= MAX_USERS) {
                System.out.println("Maximum number of users reached. Cannot add message.");
                return;
            }
            user = new ClientMessage(nickname);
            users.add(user);
        }

        user.addMessage(usuario, mensaje);
        if (user.getMessages().size() > MAX_MESSAGES) {
            user.getMessages().remove(0); // Remove the oldest message
        }

        saveMessages(users);
    }

    /**
     * Print all loaded messages from the JSON file to the console
     */
    public static void printMessages() {
        List<ClientMessage> users = loadMessages();
        List<Message> allMessages = new ArrayList<>();

        // Collect all messages from all ClientMessage objects
        for (ClientMessage user : users) {
            allMessages.addAll(user.getMessages());
        }

        // Sort messages by timestamp
        Collections.sort(allMessages, Comparator.comparing(Message::getTimestamp));

        // Print sorted messages
        if (allMessages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            //System.out.println("Loaded messages from JSON file:");
            for (Message msg : allMessages) {
                System.out.println(msg);
            }
        }
    }

    /**
     * Wrapper class for the list of users to match the JSON structure
     */
    private static class UsersWrapper {
        private List<ClientMessage> users;

        public UsersWrapper(List<ClientMessage> users) {
            this.users = users;
        }

        public List<ClientMessage> getUsers() {
            return users;
        }

        public void setUsers(List<ClientMessage> users) {
            this.users = users;
        }
    }
}