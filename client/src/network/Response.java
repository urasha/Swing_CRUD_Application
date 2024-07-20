package network;

import java.io.Serializable;

/**
 * Main object for getting data from server
 * @param message response text from server
 * @param status response server status after trying to execute command
 */
public record Response(String message, ResponseStatus status) implements Serializable {
    private static final long serialVersionUID = 192L;
}
