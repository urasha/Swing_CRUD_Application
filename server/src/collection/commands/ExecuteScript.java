package collection.commands;

import collection.CollectionManager;
import collection.CommandManager;
import data.Person;
import db.User;
import exceptions.CommandNotFoundException;
import network.Request;
import network.Response;
import network.ResponseStatus;
import utility.person.StandardPersonCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Execute a script with commands written in interactive mode
 */
public class ExecuteScript extends Command {
    private final List<String> commandsWithInputElement = List.of("insert", "update", "remove_greater", "remove_lower", "replace_if_lower");
    private int recursionCounter = 0;

    public ExecuteScript(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        String arguments = request.getCommandArguments();

        if (!checkArguments(arguments)) {
            return new Response("Некорректные аргументы для команды", ResponseStatus.ERROR);
        }

        StandardPersonCreator personCreator = new StandardPersonCreator();
        StringBuilder resultText = new StringBuilder();
        var copiedCollection = new LinkedHashMap<String, Person>(CollectionManager.getInstance().getCollection());

        try (BufferedReader reader = new BufferedReader(new FileReader(arguments))) {
            ArrayList<String> line;
            String text;

            while ((text = reader.readLine()) != null) {
                if (recursionCounter > 1) {
                    recursionCounter = 0;
                    throw new IllegalArgumentException();
                }

                recursionCounter += text.contains("execute_script " + arguments) ? 1 : 0;

                line = new ArrayList<>(Arrays.asList(text.trim().split("\\s+")));

                if (line.get(0).isEmpty()) {
                    continue;
                }

                if (!CommandManager.getInstance().getAllCommands().containsKey(line.get(0))) {
                    throw new CommandNotFoundException();
                }

                if (!commandsWithInputElement.contains(line.get(0))) {
                    resultText.append(CommandManager.getInstance().executeCommand(new Request(null, line, new User("", ""))).message()).append('\n');
                    continue;
                }

                personCreator.setIsInteractiveInput(false);

                ArrayList<String> params = new ArrayList<>(7);
                for (int i = 0; i < 7; i++) {
                    params.add(reader.readLine().trim());
                }

                personCreator.setPersonParams(params);

                resultText.append(CommandManager.getInstance().executeCommand(new Request(personCreator.create(), line, request.getUser())).message()).append('\n');

                personCreator.setIsInteractiveInput(true);
            }
        } catch (Throwable e) {
            CollectionManager.getInstance().setCollection(copiedCollection);
            return new Response("Ошибка считывания файла!", ResponseStatus.ERROR);
        } finally {
            personCreator.setIsInteractiveInput(true);
            recursionCounter = 0;
        }

        return new Response(resultText.toString(), ResponseStatus.SUCCESS);
    }

    @Override
    public boolean validateArguments(String arguments) {
        return new File(arguments).isFile();
    }
}
