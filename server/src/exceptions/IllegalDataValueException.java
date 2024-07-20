package exceptions;

/**
 * Invalid value for this data type
 */
public class IllegalDataValueException extends RuntimeException {
    @Override
    public String getMessage() {
        return  "Запрещенное значение для данных!";
    }
}
