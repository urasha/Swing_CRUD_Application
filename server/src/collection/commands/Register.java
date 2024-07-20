package collection.commands;

import data.Person;
import db.DatabaseUserHandler;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;

public class Register extends Command {
    public Register(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        String[] usernameAndPassword = arguments.split(" ");

        try {
            if (DatabaseUserHandler.getInstance().addUser(usernameAndPassword[0], usernameAndPassword[1])) {
                return new Response("Пользователь успешно зарегистрирован!", ResponseStatus.SUCCESS);
            }

            return new Response("Такой пользователь уже существует!", ResponseStatus.SUCCESS);
        } catch (SQLException e) {
            return new Response("Ошибка в запросе!!!", ResponseStatus.ERROR);
        }
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.split("\\s+").length == 2;
    }
}
