package utility;

import data.Person;
import db.User;
import db.UserManager;
import network.Request;

import java.util.ArrayList;

public class RequestBuilder {
    private ArrayList<String> command;
    private Person person;
    private User user;

    private RequestBuilder(Builder builder) {
        this.command = builder.command;
        this.person = builder.person;
        this.user = builder.user;
    }

    public static class Builder {
        private ArrayList<String> command = new ArrayList<>();
        private Person person;
        private User user;

        public Builder() {
            person = null;
            user = UserManager.getCurrentUser();
        }

        public Builder setCommand(String command) {
            this.command = new ArrayList<>();
            this.command.add(command);
            return this;
        }

        public Builder addToCommand(String command) {
            this.command.add(command);
            return this;
        }

        public Builder setPerson(Person person) {
            this.person = person;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Request build() {
            return new Request(person, command, user);
        }
    }
}
