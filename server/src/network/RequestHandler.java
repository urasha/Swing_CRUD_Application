package network;

import collection.CommandManager;

public class RequestHandler {

    public static Response handle(Request request) {
        return CommandManager.getInstance().executeCommand(request);
    }
}
