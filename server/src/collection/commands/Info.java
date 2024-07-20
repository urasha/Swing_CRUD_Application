package collection.commands;

import data.Person;
import collection.CollectionManager;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.time.LocalDate;

/**
 * Print info about collection
 */
public class Info extends Command {
    public Info(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        return new Response("Тип коллекции: LinkedHashMap\n" + "Дата инициализации: " + LocalDate.now() + '\n' + "Количество элементов: " + CollectionManager.getInstance().getCollection().size(), ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
