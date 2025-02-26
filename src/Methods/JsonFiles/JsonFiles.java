package Methods.JsonFiles;

import Methods.Client.ClientMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonFiles {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Save messages to a JSON file
     * @param clientMessage The ClientMessage object containing the messages
     * @param filePath The file path where the JSON file will be saved
     */
    public static void saveMessages(ClientMessage clientMessage, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(clientMessage, writer);
            System.out.println("Messages saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load messages from all JSON files in a directory
     * @param folderPath The directory containing JSON files
     * @return A list of ClientMessage objects with all messages
     */
    public static List<ClientMessage> loadMessagesFromDirectory(String folderPath) {
        List<ClientMessage> allMessages = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Directory not found: " + folderPath + ". Creating new directory...");
            folder.mkdirs();
            return allMessages; // Return empty list if directory does not exist
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("No JSON files found in " + folderPath);
            return allMessages;
        }

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<ClientMessage>() {}.getType();
                ClientMessage message = gson.fromJson(reader, type);
                if (message != null) {
                    allMessages.add(message);
                }
            } catch (IOException | JsonSyntaxException e) {
                System.out.println("Error reading file: " + file.getName() + " - " + e.getMessage());
            }
        }

        return allMessages;
    }

    /**
     * Print all loaded messages from a directory to the console
     * @param folderPath The directory containing JSON files
     */
    public static void printMessages(String folderPath) {
        List<ClientMessage> messages = loadMessagesFromDirectory(folderPath);
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Loaded messages from JSON files:");
            for (ClientMessage msg : messages) {
                System.out.println(msg);
            }
        }
    }
}
