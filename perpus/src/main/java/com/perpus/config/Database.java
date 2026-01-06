package com.perpus.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL =
            "jdbc:mysql://26.193.144.186:3307/db_pbo";
    private static final String USER = "root";
    private static final String PASS = "pram4848";

    private static Connection connection;

    private Database() {
        // mencegah instansiasi
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Memuat driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
        }
    }
}
