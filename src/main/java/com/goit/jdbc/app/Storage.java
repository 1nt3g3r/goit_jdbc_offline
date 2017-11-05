package com.goit.jdbc.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Storage {
    private String connectionURL = "jdbc:mysql://localhost/hib_test";
    private String user = "hib_user";
    private String pass = "hib_pass";

    private Connection connection;
    private Statement statement;

    public Storage() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL, user, pass);
            statement = connection.createStatement();

            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS DEVELOPER" +
                "(ID INT AUTO_INCREMENT PRIMARY KEY," +
                "FIRST_NAME VARCHAR(50)," +
                "LAST_NAME VARCHAR(50)," +
                "SPECIALTY VARCHAR(50))";
        statement.executeUpdate(sql);
    }

    public static void main(String[] args) {
        Storage st = new Storage();
    }
}
