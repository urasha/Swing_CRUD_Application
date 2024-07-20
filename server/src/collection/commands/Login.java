package collection.commands;

import db.DatabaseUserHandler;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;

public class Login extends Command {
    public Login(String name, String description) {
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
            if (DatabaseUserHandler.getInstance().login(usernameAndPassword[0], usernameAndPassword[1])) {
                return new Response("Успешный вход!", ResponseStatus.AUTH);
            }

            return new Response("Некорретный логин или пароль", ResponseStatus.ERROR);
        } catch (SQLException e) {
            return new Response("Ошибка в запросе!!!", ResponseStatus.ERROR);
        }
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.split("\\s+").length == 2;
    }
}
