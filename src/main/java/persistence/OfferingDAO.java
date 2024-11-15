package persistence;

import offerings.Location;
import offerings.Offering;
import offerings.Schedule;
import users.Instructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfferingDAO {

    private static Connection connection;
    private static UserDAO userDAO;

    public OfferingDAO(String dbPath, UserDAO userDAO) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            this.connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the SQLite database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        this.userDAO = userDAO;
        createTable();
    }

    private static void createTable() {
        String createOfferingsTableSQL = "CREATE TABLE IF NOT EXISTS offerings ("
                + "id INTEGER PRIMARY KEY, "
                + "location TEXT NOT NULL, "
                + "schedule TEXT NOT NULL, "
                + "lesson_type TEXT NOT NULL, "
                + "is_available BOOLEAN DEFAULT FALSE, "
                + "instructor_id INTEGER DEFAULT NULL, "
                + "FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE SET NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createOfferingsTableSQL);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables: " + e.getMessage(), e);
        }
    }


    public static boolean storeOffering(Offering offering) {
        String insertSQL = "INSERT INTO offerings (id, location, schedule, lesson_type, is_available, instructor_id) "
                + "VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setInt(1, offering.getId());
            stmt.setString(2, offering.getLocation().toString());
            stmt.setString(3, offering.getSchedule().toString());
            stmt.setString(4, offering.getLessonType());
            stmt.setBoolean(5, offering.isAvailableToPublic());
            if (offering.getInstructor() != null) {
                stmt.setInt(6, offering.getInstructor().getId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
