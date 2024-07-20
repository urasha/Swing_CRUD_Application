package collection;

import db.DatabaseUserHandler;
import exceptions.IncorrectParamsException;
import collection.commands.*;
import network.Request;
import network.Response;
import network.ResponseStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Regulates the performance of commands and executes them
 */
public class CommandManager {
    private final LinkedHashMap<String, Command> allCommands = new LinkedHashMap<>();

    private static CommandManager instance;

    private CommandManager() {
        allCommands.put("help", new Help("help", "вывести справку по доступным командам"));
        allCommands.put("info", new Info("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)"));
        allCommands.put("exit", new Exit("exit", "завершить программу (без сохранения в файл)"));
        allCommands.put("clear", new Clear("clear", "очистить коллекцию"));
        allCommands.put("show", new Show("show", "вывести в стандартный поток вывода все элементы коллекции"));
        allCommands.put("remove_key", new RemoveKey("remove_key null", "удалить элемент из коллекции по его ключу"));
        allCommands.put("insert", new Insert("insert null {element}", "добавить новый элемент с заданным ключом"));
        allCommands.put("update", new Update("update id {element}", "обновить значение элемента коллекции, id которого равен заданному"));
        allCommands.put("remove_greater", new RemoveGreater("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный"));
        allCommands.put("remove_lower", new RemoveLower("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный"));
        allCommands.put("replace_if_lower", new ReplaceIfLower("replace_if_lower null {element}", "заменить значение по ключу, если новое значение меньше старого"));
        allCommands.put("remove_all_by_hair_color", new RemoveAllByHairColor("remove_all_by_hair_color hairColor", "удалить из коллекции все элементы, значение поля hairColor которого эквивалентно заданному"));
        allCommands.put("remove_any_by_hair_color", new RemoveAnyByHairColor("remove_any_by_hair_color hairColor", "удалить из коллекции один элемент, значение поля hairColor которого эквивалентно заданному"));
        allCommands.put("filter_greater_than_height", new FilterGreaterThanHeight("filter_greater_than_height height", "вывести элементы, значение поля height которых больше заданного"));
        allCommands.put("execute_script", new ExecuteScript("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме"));
        allCommands.put("register", new Register("register {credentials}", "зарегестрировать пользователя"));
        allCommands.put("login", new Login("login {credentials}", "войти в аккаунт пользователя"));
    }

    public static synchronized CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    /**
     * Get all commands and corresponding classes
     *
     * @return Commands and corresponding classes
     */
    public LinkedHashMap<String, Command> getAllCommands() {
        return allCommands;
    }

    /**
     * Try to execute command with parameters
     *
     * @param request request
     */
    public Response executeCommand(Request request) {
        ArrayList<String> commandAndArgs = request.getCommand();

        if (request.getUser() == null && !List.of("login", "register").contains(commandAndArgs.get(0))) {
            return new Response("Чтобы начать работу зарегистрируйтесь или войдите в аккаунт", ResponseStatus.ERROR);
        }

        if (commandAndArgs.size() > 2) {
            return new Response(new IncorrectParamsException(commandAndArgs.get(0)).getMessage(), ResponseStatus.ERROR);
        }

        return allCommands.get(commandAndArgs.get(0)).execute(request);
    }
}
