package network;

import data.Person;
import db.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private static final long serialVersionUID = 228L;

    private final Person person;
    private final ArrayList<String> command;
    private final User user;

    public Request(Person person, ArrayList<String> command, User user) {
        this.person = person;
        this.command = command;
        this.user = user;
    }

    public Person getPerson() {
        return person;
    }

    public ArrayList<String> getCommand() {
        return command;
    }

    public User getUser() {
        return user;
    }

    public String getCommandArguments() {
        command.remove(0);
        return String.join(" ", command);
    }
}
