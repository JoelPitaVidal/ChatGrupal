package Methods.Client;

import java.io.Serializable;

/**
 * ClientMessage class that represents a message sent by a client.
 */
public class ClientMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nickname;
    private String message;

    /**
     * Constructor for ClientMessage.
     *
     * @param nickname Client's nickname.
     * @param message Client's message.
     **/
    public ClientMessage(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
