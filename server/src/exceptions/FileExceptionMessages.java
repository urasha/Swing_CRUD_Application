package exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Storage for file processing errors
 */
public final class FileExceptionMessages {
    public static final Map<Class<? extends Exception>, String> EXCEPTIONS = Map.of(
            FileNotFoundException.class, "Файл с заданным именем не найден!",
            IOException.class, "Непредвиденное завершение работы с файлом!",
            IllegalArgumentException.class, "Некорректный формат данных в файле!",
            DateTimeParseException.class, "Некорректный формат данных в файле!",
            IllegalDataValueException.class, new IllegalDataValueException().getMessage(),
            ArrayIndexOutOfBoundsException.class, "Не все необходимые данные были найдены в файле!"
    );

    private FileExceptionMessages() {
    }
}
