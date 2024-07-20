package collection.commands;

import data.Person;
import exceptions.UrgentStopException;
import collection.CommandManager;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Stop program execution
 */
public class Exit extends Command {
    public Exit(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        return new Response(new UrgentStopException().getMessage(), ResponseStatus.EXIT);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
