package collection.commands;

import data.Person;
import collection.CollectionManager;
import db.DatabaseCollectionHandler;
import db.DatabaseUserHandler;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Clear collection
 */
public class Clear extends Command {
    public Clear(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        try {
            DatabaseCollectionHandler.clear(request.getUser());
        } catch (SQLException e) {
            return new Response("Ошибка при выполнении запроса", ResponseStatus.ERROR);
        }

        CollectionManager.getInstance().getCollection().entrySet().removeIf(
                stringPersonEntry -> Objects.equals(
                        stringPersonEntry.getValue().getCreator(),
                        request.getUser().username()
                )
        );

        return new Response("Коллекция очищена", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
