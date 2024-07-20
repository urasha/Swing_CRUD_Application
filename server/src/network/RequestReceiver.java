package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class RequestReceiver {
    private static final int PORT = 5676;

    public void startReceiving() {
        ThreadPoolExecutor getRequestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        ThreadPoolExecutor handleRequestExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        ThreadPoolExecutor sendResponseExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(PORT));

            // main receive loop
            while (true) {
                SocketChannel socketChannel = server.accept();

                if (socketChannel == null) {
                    continue;
                }

                Future<Request> requestFuture = getRequestExecutor.submit(() -> receiveRequest(socketChannel));
                Request request;
                try {
                    request = requestFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Ошибка при работе с потоком.");
                    continue;
                }

                if (request == null) {
                    continue;
                }

                handleRequestExecutor.execute(() -> {
                    Response response;
                    response = RequestHandler.handle(request);

                    sendResponseExecutor.execute(() -> {
                        ResponseSender.sendToClient(socketChannel, response);
                    });
                });
            }
        } catch (IOException e) {
            System.out.println("Ошибка считывания объекта!");
        }
    }

    private Request receiveRequest(SocketChannel socketChannel) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            return (Request) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}
