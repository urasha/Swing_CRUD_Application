package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    public static void sendToClient(SocketChannel socketChannel, Response response) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream())) {
            objectOutputStream.writeObject(response);
        } catch (IOException e) {
            System.out.println("Ошибка отправки ответа!!!");
        }
    }
}
