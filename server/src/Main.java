import collection.CollectionManager;
import db.DatabaseConnection;
import network.RequestReceiver;

public class Main {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер для БД не установлен");
            return;
        }

        DatabaseConnection.connectDatabase(args[0]);
        CollectionManager.getInstance().tryCreateCollection();

        while (true) {
            new RequestReceiver().startReceiving();
        }
    }
}
