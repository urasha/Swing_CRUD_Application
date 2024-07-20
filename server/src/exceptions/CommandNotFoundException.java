package exceptions;

/**
 * The entered command was not found in the specified
 */
public class CommandNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Неизвестная команда! Чтобы увидеть список всех команд введите %s"
                .formatted("help");
    }
}
