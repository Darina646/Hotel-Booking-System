package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB implements IDB {
    private final String url;
    private final String username;
    private final String password;

    public PostgresDB(String url, String username, String password) {
        this.url = "jdbc:postgresql://localhost:5432/hotel_booking_system";
        this.username = "postgres";
        this.password = "0000";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}