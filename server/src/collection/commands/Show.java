package collection.commands;

import collection.CollectionManager;
import data.Person;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Print all collection objects and their keys
 */
public class Show extends Command {
    public Show(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        var collection = CollectionManager.getInstance().getCollection();

        if (collection.isEmpty()) {
            return new Response("Коллекция пуста", ResponseStatus.SUCCESS);
        }

        StringBuilder result = new StringBuilder();
        for (String key : collection.keySet()) {
            result.append(collection.get(key)).append(",%s,%s".formatted(key, collection.get(key).getCreator())).append(" ");
        }

        return new Response(result.toString(), ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
