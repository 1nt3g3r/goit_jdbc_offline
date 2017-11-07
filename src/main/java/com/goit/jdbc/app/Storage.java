package com.goit.jdbc.app;

import com.goit.jdbc.app.dao.DeveloperDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage implements DeveloperDAO {
    private String connectionURL = "jdbc:mysql://localhost/hib_test";
    private String user = "hib_user";
    private String pass = "hib_pass";

    private Connection connection;
    private Statement statement;

    private PreparedStatement selectSt;
    private PreparedStatement updateSt;
    private PreparedStatement insertSt;

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

            insertSt = connection.
      prepareStatement("INSERT INTO DEVELOPER (FIRST_NAME, LAST_NAME) VALUES(?, ?)");

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
        for(Developer developer : devs) {
            String sql = "INSERT INTO DEVELOPER (FIRST_NAME, LAST_NAME) VALUES ('" +
                    developer.getFirstName() + "', '" + developer.getLastName() + "')";

            try {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDevelopersBatched(List<Developer> devs) throws SQLException {
        connection.setAutoCommit(false);

        for(Developer developer : devs) {
            String sql = "INSERT INTO DEVELOPER (FIRST_NAME, LAST_NAME) VALUES ('" +
                    developer.getFirstName() + "', '" + developer.getLastName() + "')";
            statement.addBatch(sql);
        }
        statement.executeBatch();

        connection.commit();


    }

    public void createDeveloper(Developer developer) {
        String firstName = developer.getFirstName();
        String lastName = developer.getLastName();

        try {
            insertSt.setString(1, firstName);
            insertSt.setString(2, lastName);
            insertSt.executeUpdate();
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

    public void deleteDeveloper(long id) {
        String deleteQuery =
                "DELETE FROM DEVELOPER WHERE ID=" + id;

        try {
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
        }
    }

    public void updateDeveloper(Developer developer) {
        try {
            updateSt.setString(1, developer.getFirstName());
            updateSt.setString(2, developer.getLastName());
            updateSt.setLong(3, developer.getId());
            updateSt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Developer> listDevelopers() {
        String selectAllQuery = "SELECT FIRST_NAME, LAST_NAME FROM DEVELOPER";

        List<Developer> developers = new ArrayList<Developer>();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(selectAllQuery);

            while (rs.next()) {
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");

                Developer developer = new Developer();
                developer.setFirstName(firstName);
                developer.setLastName(lastName);

                developers.add(developer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
        }

        return developers;
    }

    public void clearDatabase() {
        String deleteQuery = "TRUNCATE DEVELOPER";
        try {
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Storage storage = new Storage();

        List<Developer> devs = new ArrayList<Developer>();
        for (int i = 0; i < 100; i++) {
            Developer developer = new Developer();
            developer.setFirstName("FirstName " + i);
            developer.setLastName("LastName " + i);
            devs.add(developer);
        }

        long startTime = System.currentTimeMillis();
        storage.addDevelopers(devs);
        storage.addDevelopersBatched(devs);
        long endTime = System.currentTimeMillis();

        long diff = endTime - startTime;
        System.out.println("TIME: " + diff);


    }
}
