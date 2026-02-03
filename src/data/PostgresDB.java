package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB implements IDB {
    private static PostgresDB instance;
    private final String url = "jdbc:postgresql://localhost:5432/hotel_booking_system";
    private final String username = "postgres";
    private final String password = "0000";

    // Private constructor to prevent instantiation
    private PostgresDB() { }

    // Singleton getInstance method
    public static synchronized PostgresDB getInstance() {
        if (instance == null) {
            instance = new PostgresDB();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
