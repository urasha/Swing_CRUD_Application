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
 * Replace value by key if new value is less than old
 */
public class ReplaceIfLower extends Command {
    public ReplaceIfLower(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();
        String key = arguments.trim();

        Person person = request.getPerson();
        Person oldPerson = collection.get(key);
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

        if (newPerson.compareTo(oldPerson) < 0) {
            try {
                DatabaseCollectionHandler.replaceLower(key, newPerson, request.getUser());

                if (collection.get(key).getCreator().equals(request.getUser().username())) {
                    collection.put(key, newPerson);
                }
            } catch (SQLException e) {
                return new Response(new DatabaseQueryException().getMessage(), ResponseStatus.ERROR);
            }

            return new Response("Объект заменен", ResponseStatus.SUCCESS);
        }

        return new Response("Объект не заменен. Новое значение не меньше значения, заданного по ключу", ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.split("\\s+").length == 1;
    }
}
