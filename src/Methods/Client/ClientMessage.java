package Methods.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClientMessage class that represents a message sent by a client.
 */
public class ClientMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nickname;
    private List<Message> messages;

    /**
     * Constructor for ClientMessage.
     *
     * @param nickname Client's nickname.
     */
    public ClientMessage(String nickname) {
        this.nickname = nickname;
        this.messages = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(String usuario, String mensaje) {
        if (messages.size() >= 10) {
            messages.remove(0); // Remove the oldest message if we already have 10
        }
        messages.add(new Message(usuario, mensaje, new Date()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nickname + ":\n");
        for (Message message : messages) {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }
}