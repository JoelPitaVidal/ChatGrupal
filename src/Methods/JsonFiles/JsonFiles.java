package Methods.Utils;

import Methods.Client.ClientMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
public class JsonFiles {
    /**
     * Save messages to a JSON file
     * @param clientMessage The ClientMessage object containing the messages
     * @param filePath The file path where the JSON file will be saved
     */
    public static void saveMessages(ClientMessage clientMessage, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(clientMessage, writer);
            System.out.println("Messages saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load messages from a JSON file
     * @param filePath The file path where the JSON file is located
     * @return The ClientMessage object containing the messages
     */
    public static ClientMessage loadMessages(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                System.out.println("Messages JSON not found, creating new file...");
                file.createNewFile();
                return new ClientMessage(""); // Return an empty ClientMessage object if the file does not exist
            } catch (IOException e) {
                System.out.println("Error creating messages file: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<ClientMessage>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error loading messages: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
