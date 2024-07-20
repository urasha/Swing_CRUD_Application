package db;

import data.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseCollectionHandler {
    private static final String GET_PERSONS_REQUEST = "SELECT * FROM person";
    private static final String DELETE_GREATER_REQUEST = "DELETE FROM person WHERE name > ? AND creator = ?";
    private static final String DELETE_LOWER_REQUEST = "DELETE FROM person WHERE name < ? AND creator = ?";
    private static final String DELETE_ALL_BY_HAIR_COLOR_REQUEST = "DELETE FROM person WHERE haircolor = ? AND creator = ?";
    private static final String DELETE_ANY_BY_HAIR_COLOR_REQUEST = "DELETE FROM person WHERE id IN (SELECT id FROM person WHERE haircolor = ? LIMIT 1) AND creator = ?";
    private static final String DELETE_BY_KEY_REQUEST = "DELETE FROM person WHERE keyname = ? AND creator = ?";
    private static final String REPLACE_LOWER_REQUEST =
            "UPDATE person " +
                    "SET name = ?, coordinates = ?, height = ?, eyecolor = ?, haircolor = ?, nationality = ?, location = ? " +
                    "WHERE keyname = ? AND creator = ?";
    private static final String UPDATE_PERSON_REQUEST =
            "UPDATE person " +
                    "SET name = ?, coordinates = ?, height = ?, eyecolor = ?, haircolor = ?, nationality = ?, location = ? " +
                    "WHERE creator = ?";
    private static final String CLEAR_REQUEST = "DELETE FROM person WHERE creator = ?";
    private static final String ADD_PERSON_REQUEST =
            "INSERT INTO person(name, coordinates, creationdate, height, eyecolor, haircolor, nationality, location, keyName, creator) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_PERSON_BY_KEY = "SELECT id FROM person WHERE keyname = ?";

    public static synchronized void update(Person person, User user) throws SQLException {
        try (PreparedStatement updateStatement = DatabaseConnection.getConnection().prepareStatement(UPDATE_PERSON_REQUEST)) {
            setUpdateParams(person, updateStatement);
            updateStatement.setString(8, user.username());
            updateStatement.executeUpdate();
        }
    }

    public static synchronized int add(String key, Person person) throws SQLException {
        try (PreparedStatement addStatement = DatabaseConnection.getConnection().prepareStatement(ADD_PERSON_REQUEST)) {
            addStatement.setString(1, person.getName());
            addStatement.setString(2, person.getCoordinates().toString());
            addStatement.setDate(3, Date.valueOf(person.getCreationDate()));
            addStatement.setInt(4, person.getHeight());
            addStatement.setString(5, person.getEyeColor().toString());
            addStatement.setString(6, person.getHairColor().toString());
            addStatement.setString(7, person.getNationality().toString());
            addStatement.setString(8, person.getLocation().toString());
            addStatement.setString(9, key);
            addStatement.setString(10, person.getCreator());

            addStatement.executeUpdate();
        }

        try (PreparedStatement getStatement = DatabaseConnection.getConnection().prepareStatement(GET_PERSON_BY_KEY)) {
            getStatement.setString(1, key);
            ResultSet result = getStatement.executeQuery();
            result.next();
            return result.getInt("id");
        }
    }

    public static synchronized void clear(User currentUser) throws SQLException {
        try (PreparedStatement clearStatement = DatabaseConnection.getConnection().prepareStatement(CLEAR_REQUEST)) {
            clearStatement.setString(1, currentUser.username());
            clearStatement.executeUpdate();
        }
    }

    public static synchronized void deleteAllByHairColor(String color, User user) throws SQLException {
        try (PreparedStatement deleteStatement = DatabaseConnection.getConnection().prepareStatement(DELETE_ALL_BY_HAIR_COLOR_REQUEST)) {
            deleteStatement.setString(1, color);
            deleteStatement.setString(2, user.username());
            deleteStatement.executeUpdate();
        }
    }

    public static synchronized void deleteAnyByHairColor(String color, User user) throws SQLException {
        try (PreparedStatement deleteStatement = DatabaseConnection.getConnection().prepareStatement(DELETE_ANY_BY_HAIR_COLOR_REQUEST)) {
            deleteStatement.setString(1, color);
            deleteStatement.setString(2, user.username());
            deleteStatement.executeUpdate();
        }
    }

    public static synchronized void deleteGreater(String name, User user) throws SQLException {
        try (PreparedStatement deleteStatement = DatabaseConnection.getConnection().prepareStatement(DELETE_GREATER_REQUEST)) {
            deleteStatement.setString(1, name);
            deleteStatement.setString(2, user.username());
            deleteStatement.executeUpdate();
        }
    }

    public static synchronized void deleteLower(String name, User user) throws SQLException {
        try (PreparedStatement deleteStatement = DatabaseConnection.getConnection().prepareStatement(DELETE_LOWER_REQUEST)) {
            deleteStatement.setString(1, name);
            deleteStatement.setString(2, user.username());
            deleteStatement.executeUpdate();
        }
    }

    public static synchronized void deleteByKeyName(String key, User user) throws SQLException {
        try (PreparedStatement deleteStatement = DatabaseConnection.getConnection().prepareStatement(DELETE_BY_KEY_REQUEST)) {
            deleteStatement.setString(1, key);
            deleteStatement.setString(2, user.username());
            deleteStatement.executeUpdate();
        }
    }

    public static synchronized void replaceLower(String key, Person person, User user) throws SQLException {
        try (PreparedStatement updateStatement = DatabaseConnection.getConnection().prepareStatement(REPLACE_LOWER_REQUEST)) {
            setUpdateParams(person, updateStatement);
            updateStatement.setString(8, key);
            updateStatement.setString(9, user.username());
            updateStatement.executeUpdate();
        }
    }

    private static void setUpdateParams(Person person, PreparedStatement updateStatement) throws SQLException {
        updateStatement.setString(1, person.getName());
        updateStatement.setString(2, person.getCoordinates().toString());
        updateStatement.setInt(3, person.getHeight());
        updateStatement.setString(4, person.getEyeColor().toString());
        updateStatement.setString(5, person.getHairColor().toString());
        updateStatement.setString(6, person.getNationality().toString());
        updateStatement.setString(7, person.getLocation().toString());
    }

    public synchronized static LinkedHashMap<String, Person> loadCollection() {
        LinkedHashMap<String, Person> collection = new LinkedHashMap<>();
        try (PreparedStatement getPersonsStatement = DatabaseConnection.getConnection().prepareStatement(GET_PERSONS_REQUEST)) {
            ResultSet result = getPersonsStatement.executeQuery();

            while (result.next()) {
                Map.Entry<String, Person> keyAndPerson = createPersonFromDB(result);
                collection.put(keyAndPerson.getKey(), keyAndPerson.getValue());
            }

            result.close();
            System.out.println("Коллекция успешно создана");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Ошибка при загрузке из базы данных!!!");
            System.exit(0);
        }

        return collection;
    }

    private static Map.Entry<String, Person> createPersonFromDB(ResultSet data) throws SQLException, IllegalArgumentException {
        int id = data.getInt("id");
        String name = data.getString("name");
        Coordinates coordinates = new Coordinates(data.getString("coordinates"));
        LocalDate creationDate = LocalDate.parse(data.getString("creationDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int height = data.getInt("height");
        EyeColor eyeColor = EyeColor.valueOf(data.getString("eyeColor"));
        HairColor hairColor = HairColor.valueOf(data.getString("hairColor"));
        Country nationality = Country.valueOf(data.getString("nationality"));
        Location location = new Location(data.getString("location"));
        String keyName = data.getString("keyname");
        String creator = data.getString("creator");

        Person person = new Person(id, creationDate, name, coordinates, height, eyeColor, hairColor, nationality, location);
        person.setCreator(creator);

        return Map.entry(keyName, person);
    }
}
