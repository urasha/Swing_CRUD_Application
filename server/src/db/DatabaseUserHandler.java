package db;

import utility.DataHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserHandler {
    private static final String ADD_USER_REQUEST = "INSERT INTO client(username, password) VALUES (?, ?)";
    private static final String GET_USER_REQUEST = "SELECT * FROM client WHERE username = ?";
    private static final String LOGIN_USER_REQUEST = "SELECT * FROM client WHERE username = ? AND password = ?";

    private final Connection connection;

    private static DatabaseUserHandler instance;

    private DatabaseUserHandler() {
        connection = DatabaseConnection.getConnection();
    }

    public static synchronized DatabaseUserHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseUserHandler();
        }
        return instance;
    }
    public boolean login(String username, String password) throws SQLException {
        try (PreparedStatement getStatement = connection.prepareStatement(LOGIN_USER_REQUEST)) {
            getStatement.setString(1, username);
            getStatement.setString(2, DataHasher.encrypt(password, "SHA-384"));
            ResultSet result = getStatement.executeQuery();

            return result.next();
        }
    }

    public boolean addUser(String username, String password) throws SQLException {
        if (checkUserRegistered(username)) {
            return false;
        }

        try (PreparedStatement addStatement = connection.prepareStatement(ADD_USER_REQUEST)) {
            addStatement.setString(1, username);
            addStatement.setString(2, DataHasher.encrypt(password, "SHA-384"));
            addStatement.executeUpdate();
            return true;
        }
    }

    private boolean checkUserRegistered(String username) throws SQLException {
        try (PreparedStatement getStatement = connection.prepareStatement(GET_USER_REQUEST)) {
            getStatement.setString(1, username);
            ResultSet result = getStatement.executeQuery();
            return result.next();
        }
    }
}
