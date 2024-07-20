package collection.commands;

import data.Person;
import db.DatabaseCollectionHandler;
import db.DatabaseUserHandler;
import exceptions.DatabaseQueryException;
import exceptions.UrgentStopException;
import collection.CollectionManager;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Add an object to the collection with the given key
 */
public class Insert extends Command {
    public Insert(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();
        Person person = request.getPerson();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();

        if (collection.containsKey(arguments)) {
            return new Response("Объект с таким ключом уже существует!", ResponseStatus.ERROR);
        }

        try {
            Person updatedPerson = new Person(person.getName(), person.getCoordinates(), person.getHeight(), person.getEyeColor(), person.getHairColor(), person.getNationality(), person.getLocation());
            updatedPerson.setCreator(request.getUser().username());

            int dbId = DatabaseCollectionHandler.add(arguments, updatedPerson);
            updatedPerson.setId(dbId);

            collection.put(arguments, updatedPerson);
        } catch (NoSuchElementException e) {
            return new Response(new UrgentStopException().getMessage(), ResponseStatus.ERROR);
        } catch (SQLException e) {
            return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
        }

        return new Response("Новый Person успешно добавлен в коллекцию", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.split("\\s+").length == 1;
    }
}
