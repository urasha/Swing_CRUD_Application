package collection.commands;

import data.Person;
import collection.CommandManager;
import network.Request;
import network.Response;
import network.ResponseStatus;

/**
 * Print info about all commands
 */
public class Help extends Command {
    public Help(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        if (!checkArguments(request.getCommandArguments())) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        StringBuilder stringBuilder = new StringBuilder();
        CommandManager.getInstance().getAllCommands().forEach((key, value) -> stringBuilder.append(value.getInfo()).append('\n'));

        return new Response(stringBuilder.toString(), ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return arguments.isEmpty();
    }
}
