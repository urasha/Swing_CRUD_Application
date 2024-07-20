package collection.commands;

import collection.CollectionManager;
import db.DatabaseCollectionHandler;
import exceptions.DatabaseQueryException;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;

/**
 * Removes an object from a collection using a given key
 */
public class RemoveKey extends Command {
    public RemoveKey(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        String arg = arguments.trim();
        var collection = CollectionManager.getInstance().getCollection();

        if (!collection.containsKey(arg)) {
            return new Response("Указанного ключа в коллекции не найдено", ResponseStatus.ERROR);
        }

        try {
            DatabaseCollectionHandler.deleteByKeyName(arg, request.getUser());
            collection.entrySet().removeIf(stringPersonEntry -> stringPersonEntry.getValue().getCreator().equals(request.getUser().username())
                    && stringPersonEntry.getKey().equals(arg));
        } catch (SQLException e) {
            return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
        }

        return new Response("Операция завершилась", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.split("\\s+").length == 1;
    }
}
