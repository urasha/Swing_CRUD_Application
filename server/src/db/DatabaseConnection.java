package db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/studs";

    private static Connection connection;

    private static String username;
    private static String password;

    private static void terminate(String message) {
        if (message != null) {
            System.out.println(message);
        }
        System.exit(-1);
    }

    private static void setLoginCredentials(String filename) {
        try {
            Scanner loginInput = new Scanner(new FileReader(filename));

            try {
                username = loginInput.nextLine().trim();
                password = loginInput.nextLine().trim();
            } catch (NoSuchElementException e) {
                terminate("Некорретное заданы данные в файле!");
            }
        } catch (FileNotFoundException e) {
            terminate("Файл с данными для входа не найден!");
        }
    }

    public static void connectDatabase(String filename) {
        setLoginCredentials(filename);

        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Соединение с БД установлено!");
        } catch (SQLException e) {
            terminate("Ошибка подключения к БД!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
