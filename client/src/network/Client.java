package network;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final int PORT = 5676;

    public Response sendRequest(Request request) throws IOException {
        try (Socket client = new Socket("localhost", PORT)) {
            RequestSender.send(client, request);
            return ResponseReceiver.receive(client);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
