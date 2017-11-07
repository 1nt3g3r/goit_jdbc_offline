package com.goit.jdbc.app;

import java.sql.*;
import java.util.List;

public class Storage {
    private String connectionURL = "jdbc:mysql://localhost/hib_test";
    private String user = "hib_user";
    private String pass = "hib_pass";

    private Connection connection;
    private Statement statement;

    private PreparedStatement selectSt;
    private PreparedStatement updateSt;

    public Storage() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }

        try {
            connection = DriverManager.getConnection(connectionURL, user, pass);
            statement = connection.createStatement();

            selectSt =
    connection.prepareStatement("SELECT FIRST_NAME, LAST_NAME, ID FROM DEVELOPER WHERE ID=?");

            updateSt =
                    connection.prepareStatement("UPDATE DEVELOPER SET FIRST_NAME=?, LAST_NAME=? WHERE ID=?");

            createTable();
        } catch (SQLException e) {
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

    public void addDevelopers(List<Developer> devs) {
        for(Developer dev: devs) {
            createDeveloper(dev);
        }
    }

    public void createDeveloper(Developer developer) {
        List<Developer> devs = null;
        for(Developer dev : devs) {

            String firstName = developer.getFirstName();
            String lastName = developer.getLastName();

            String insertQuery =
                    "INSERT INTO DEVELOPER(FIRST_NAME, LAST_NAME)" +
                            "VALUES('" + firstName + "', '" + lastName + "')";
        }

        statement.executeBatch();




        try {
            statement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Developer getDeveloper(long id) {
        ResultSet resultSet = null;
        try {
            selectSt.setLong(1, id);
            resultSet = selectSt.executeQuery();

            if (resultSet.first()) {
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName =resultSet.getString("LAST_NAME");
                long devId = resultSet.getLong("ID");

                Developer result = new Developer();
                result.setFirstName(firstName);
                result.setLastName(lastName);
                result.setId(devId);

                return result;
            } else {
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deleteDeveloper(long id) {
        String deleteQuery =
                "DELETE FROM DEVELOPER WHERE ID=" + id;

        try {
            statement.executeUpdate(deleteQuery);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateDeveloper(Developer developer) {
        try {
            updateSt.setString(1, developer.getFirstName());
            updateSt.setString(2, developer.getLastName());
            updateSt.setLong(3, developer.getId());
            updateSt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Storage storage = new Storage();

        Developer developer = storage.getDeveloper(3);
        developer.setFirstName("MAXIM");
        storage.updateDeveloper(developer);

//        Developer dev = new Developer();
//        dev.setFirstName("Ivan");
//        dev.setLastName("Melnichuk");
//        storage.createDeveloper(dev);
        //storage.deleteDeveloper(1);

//        Developer dev = storage.getDeveloper(1);
//        System.out.println(dev.getFirstName() + ", " + dev.getLastName());

    }
}
