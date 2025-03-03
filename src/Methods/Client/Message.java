package Methods.Client;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String usuario;
    private String mensaje;
    private Date timestamp;

    public Message(String usuario, String mensaje, Date timestamp) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + usuario + ": " + mensaje;
    }
}