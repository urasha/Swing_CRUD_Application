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
 * Overwrites a collection object with the given key
 */
public class Update extends Command {
    public Update(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        int id = Integer.parseInt(arguments);
        var collection = CollectionManager.getInstance().getCollection();

        for (String key : collection.keySet()) {
            if (collection.get(key).getId() == id && collection.get(key).getCreator().equals(request.getUser().username())) {
                Person oldPerson = collection.get(key);
                Person newPerson = getNewPerson(request.getPerson(), oldPerson);

                try {
                    DatabaseCollectionHandler.update(newPerson, request.getUser());
                    collection.put(key, newPerson);
                } catch (SQLException e) {
                    return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
                }

                return new Response("Объект успешно обновлен!", ResponseStatus.SUCCESS);
            }
        }

        return new Response("Элемента с таким ID нет в коллекции", ResponseStatus.SUCCESS);
    }

    private Person getNewPerson(Person person, Person oldPerson) {
        Person newPerson = new Person(
                oldPerson.getId(),
                oldPerson.getCreationDate(),
                person.getName(),
                person.getCoordinates(),
                person.getHeight(),
                person.getEyeColor(),
                person.getHairColor(),
                person.getNationality(),
                person.getLocation()
        );
        newPerson.setCreator(oldPerson.getCreator());
        return newPerson;
    }

    @Override
    public boolean validateArguments(String arguments) {
        try {
            int id = Integer.parseInt(arguments);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
