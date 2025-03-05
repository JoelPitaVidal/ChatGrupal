# Chat Grupal

Este proyecto es una aplicación de chat grupal que permite a múltiples usuarios conectarse a un servidor y comunicarse entre sí en tiempo real. La aplicación utiliza sockets para la comunicación entre el cliente y el servidor, y Swing para la interfaz gráfica de usuario (GUI).

## Características

- Conexión de múltiples usuarios a un servidor.
- Envío y recepción de mensajes en tiempo real.
- Interfaz gráfica de usuario (GUI) para una experiencia de chat similar a las aplicaciones de mensajería.
- Comandos especiales para mostrar ayuda, salir del chat, y estados de usuario (AFK, ocupado).
- Persistencia de mensajes en archivos JSON.

## Requisitos

- Java 8 o superior.
- Biblioteca Gson para la serialización y deserialización de JSON.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes y clases:

### Paquete `Client`

- **Client.java**: Clase principal del cliente que maneja la conexión al servidor y la lógica de envío/recepción de mensajes.
- **ChatClientGUI.java**: Clase que define la interfaz gráfica de usuario (GUI) del cliente de chat.
- **ClientMessage.java**: Clase que representa los mensajes enviados por el cliente.

### Paquete `Server`

- **Server.java**: Clase principal del servidor que maneja las conexiones de los clientes y la lógica de envío/recepción de mensajes.
- **ServerMethods.java**: Clase que contiene métodos auxiliares para manejar la comunicación con los clientes.

### Paquete `Methods.JsonFiles`

- **JsonFiles.java**: Clase que maneja la carga y guardado de mensajes en archivos JSON.

## Uso

### Iniciar el Servidor

1. Ejecuta la clase `Server` para iniciar el servidor.
2. Introduce la ip y puerto en el que el servidor escuchará las conexiones.
3. Escoge el NIckname y empieza a Chatear
   
```

