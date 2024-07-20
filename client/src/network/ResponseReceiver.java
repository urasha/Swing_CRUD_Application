package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Class for getting responses from server
 */
public class ResponseReceiver {
    /**
     * @param socket socket connection to server
     * @return response object from server
     */
    public static Response receive(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        return (Response) input.readObject();
    }
}
