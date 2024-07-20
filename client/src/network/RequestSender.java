package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class for sending requests to server
 */
public class RequestSender {
    /**
     * Send request to server with exact socket connection
     * @param socket socket connection to server
     * @param request command request
     */
    public static void send(Socket socket, Request request) throws IOException {
        ObjectOutputStream outputToServer = new ObjectOutputStream(socket.getOutputStream());
        outputToServer.writeObject(request);
    }
}
