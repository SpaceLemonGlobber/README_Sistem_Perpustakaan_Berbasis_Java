package com.perpus.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL =
            "jdbc:mysql://localhost:3306/readmeperpustakaan?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    private static Connection connection;

    private Database() {
        // mencegah instansiasi
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASS);
        }
        return connection;
    }
}
