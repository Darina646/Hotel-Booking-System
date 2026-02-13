package util;

import data.IDB;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager implements IDB {
    private static DatabaseConnectionManager instance;
    private final String url;
    private final String username;
    private final String password;

    private DatabaseConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static synchronized DatabaseConnectionManager getInstance(String url, String username, String password) {
        if (instance == null) {
            instance = new DatabaseConnectionManager(url, username, password);
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return java.sql.DriverManager.getConnection(url, username, password);
    }
}