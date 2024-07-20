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
 * Removes first collection object whose hairColor is equal to the given one
 */
public class RemoveAnyByHairColor extends Command {
    public RemoveAnyByHairColor(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();
        for (String key : collection.keySet()) {
            if (collection.get(key).getHairColor().name().equals(arguments) && collection.get(key).getCreator().equals(request.getUser().username())) {
                try {
                    DatabaseCollectionHandler.deleteAnyByHairColor(arguments, request.getUser());
                    collection.remove(key);
                } catch (SQLException e) {
                    return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
                }
                return new Response("Операция завершена успешно!", ResponseStatus.SUCCESS);
            }
        }

        return new Response("Ничего не было удалено", ResponseStatus.SUCCESS);
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
