package userinterface;

import offerings.Location;
import offerings.Offering;
import offerings.OfferingCatalog;
import offerings.Schedule;
import persistence.OfferingDAO;
import persistence.UserDAO;
import users.Admin;

import java.sql.*;

// TODO: add UI methods to allow user to interact with system
public class Console {
    private final OfferingDAO offeringDAO;
    private final UserDAO userDAO;

    public Console(OfferingDAO offeringDAO, UserDAO userDAO) {
        this.offeringDAO = offeringDAO;
        this.userDAO = userDAO;
    }

    // Method to process offerings for an admin
    public void processOfferingsAdmin(Admin admin) {
        System.out.println("Processing offerings for Admin: " + admin.getName());
    }

    // Method to create a new offering
    public void makeNewOffering(Location location, Schedule schedule, String lessonType) {
        Offering newOffering = OfferingCatalog.createOffering(location, schedule, lessonType);
        if (newOffering != null) {
            System.out.println("New offering created successfully!");
        } else {
            System.out.println("Offering already exists.");
        }
    }

    public static void main(String[] args) {

        // The SQLite database file location
        String url = "jdbc:sqlite:sample.db";

        try (Connection conn = DriverManager.getConnection(url)) {

            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");

                String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                        + "id INTEGER PRIMARY KEY, "
                        + "name TEXT NOT NULL, "
                        + "age INTEGER NOT NULL"
                        + ");";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                }

                String insertSQL = "INSERT INTO users (name, age) VALUES ('Alice', 30);";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(insertSQL);
                }

                String selectSQL = "SELECT id, name, age FROM users;";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSQL)) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Age: " + rs.getInt("age"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
