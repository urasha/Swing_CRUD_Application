package collection.commands;

import collection.CollectionManager;
import network.Request;
import network.Response;
import network.ResponseStatus;

/**
 * Prints collection objects whose height is greater than the specified value
 */
public class FilterGreaterThanHeight extends Command {
    public FilterGreaterThanHeight(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        StringBuilder stringBuilder = new StringBuilder();
        int height = Integer.parseInt(arguments);
        var collection = CollectionManager.getInstance().getCollection();
        collection
                .entrySet()
                .stream()
                .filter((keyAndPerson -> keyAndPerson.getValue().getHeight() > height))
                .forEach((keyAndPerson -> stringBuilder.append(keyAndPerson.getValue()).append(",%s,%s".formatted(keyAndPerson.getKey(), keyAndPerson.getValue().getCreator())).append(" ")));

        return new Response(stringBuilder.toString(), ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        try {
            int height = Integer.parseInt(arguments);
            return height > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
