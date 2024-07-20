package collection.commands;

import data.HairColor;
import data.Person;
import collection.CollectionManager;
import db.DatabaseCollectionHandler;
import exceptions.DatabaseQueryException;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;

/**
 * Removes all collection objects whose hairColor is equal to the given one
 */
public class RemoveAllByHairColor extends Command {
    public RemoveAllByHairColor(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();

        try {
            DatabaseCollectionHandler.deleteAllByHairColor(arguments, request.getUser());
            collection.entrySet().removeIf(stringPersonEntry -> stringPersonEntry.getValue().getHairColor().name().equals(arguments)
                    && stringPersonEntry.getValue().getCreator().equals(request.getUser().username()));
        } catch (SQLException e) {
            return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
        }

        return new Response("Операция завершена успешно!", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        try {
            HairColor.valueOf(arguments);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
