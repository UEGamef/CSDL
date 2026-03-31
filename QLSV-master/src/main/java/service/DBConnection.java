package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=QLSV;encrypt=true;trustServerCertificate=true";
    private static final String USER = "test";
    private static final String PASSWORD = "123456";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}