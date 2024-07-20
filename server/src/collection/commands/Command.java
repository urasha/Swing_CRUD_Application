package collection.commands;

import network.Request;
import network.Response;

import java.util.Objects;

/**
 * Template for all commands
 */
abstract public class Command {
    /**
     * Checks the validity of the arguments
     * @param arguments Command parameters
     * @return The result of checking the arguments for correctness
     */
    abstract public boolean validateArguments(String arguments);

    /**
     * Executes the command
     * @param request command request
     */
    abstract public Response execute(Request request);

    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Command's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Formatted name + description
     */
    public String getInfo() {
        return name + " : " + description;
    }

    protected boolean checkArguments(String arguments) {
        arguments = arguments.trim();
        return validateArguments(arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
