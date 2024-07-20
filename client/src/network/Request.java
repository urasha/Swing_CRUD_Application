package network;

import data.Person;
import db.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Main object for sending command to server
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 228L;

    private final Person person;
    private final ArrayList<String> command;
    private final User user;

    /**
     * @param person Person instance, if command need it
     * @param command command name with arguments
     * @param user Current user instance
     */
    public Request(Person person, ArrayList<String> command, User user) {
        this.person = person;
        this.command = command;
        this.user = user;
    }

}
