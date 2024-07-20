package exceptions;

/**
 * Invalid parameters for the command
 */
public class IncorrectParamsException extends RuntimeException {
    private final String name;

    public IncorrectParamsException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Некорректые аргументы для команды \033[3m%s\033[0m".formatted(name.split(" ")[0]);
    }
}
