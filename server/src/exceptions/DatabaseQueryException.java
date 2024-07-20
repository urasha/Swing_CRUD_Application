package exceptions;

public class DatabaseQueryException extends Exception {
    @Override
    public String getMessage() {
        return  "Ошибка выполнения запроса к БД";
    }
}
