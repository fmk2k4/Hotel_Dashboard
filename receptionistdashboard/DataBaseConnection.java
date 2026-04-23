package com.mycompany.receptionistdashboard;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DataBaseConnection() {
    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (Exception exception) {
            System.out.println("Database connection failed.");
            exception.printStackTrace();
        }

        return connection;
    }
}