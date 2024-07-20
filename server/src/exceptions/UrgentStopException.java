package exceptions;

/**
 * Urgent program stop
 */
public class UrgentStopException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Завершение работы...";
    }
}
