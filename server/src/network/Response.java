package network;

import java.io.Serializable;

public record Response(String message, ResponseStatus status) implements Serializable {
    private static final long serialVersionUID = 192L;

}
