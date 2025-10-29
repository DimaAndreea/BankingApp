package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/proiect_oop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Connection failed: " + e.getMessage());
            throw new RuntimeException("Could not connect to database", e);
        } catch (ClassNotFoundException e) {
            System.err.println("[DB ERROR] PostgreSQL driver not found");
            throw new RuntimeException("Database driver missing", e);
        }
    }
}
