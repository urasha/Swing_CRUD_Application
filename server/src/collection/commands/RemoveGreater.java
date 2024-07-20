package collection.commands;

import data.Person;
import collection.CollectionManager;
import db.DatabaseCollectionHandler;
import exceptions.DatabaseQueryException;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;

/**
 * Removes all collection objects larger than the specified object
 */
public class RemoveGreater extends Command {
    public RemoveGreater(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();
        Person person = request.getPerson();

        try {
            DatabaseCollectionHandler.deleteGreater(person.getName(), request.getUser());
            collection.entrySet().removeIf(keyAndPerson -> keyAndPerson.getValue().compareTo(person) > 0
                    && keyAndPerson.getValue().getCreator().equals(request.getUser().username()));
        } catch (SQLException e) {
            return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.SUCCESS);
        }


        return new Response("Операция завершена успешно!", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
