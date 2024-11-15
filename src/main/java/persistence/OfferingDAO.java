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

    public static Optional<Offering> getOfferingById(int id) {
        String selectSQL = "SELECT id, location, schedule, lesson_type, is_available, instructor_id FROM offerings WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int offeringId = rs.getInt("id");
                String locationStr = rs.getString("location");
                String scheduleStr = rs.getString("schedule");
                String lessonType = rs.getString("lesson_type");
                boolean isAvailable = rs.getBoolean("is_available");
                int instructorId = rs.getInt("instructor_id");

                Instructor instructor = null;
                if (!rs.wasNull()) {
                    instructor = userDAO.getInstructorById(instructorId).orElse(null);
                }

                try {
                    Location location = Location.fromString(locationStr);
                    Schedule schedule = Schedule.fromString(scheduleStr);
                    Offering offering = new Offering(offeringId, location, schedule, lessonType, isAvailable, instructor);
                    return Optional.of(offering);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid location: " + locationStr + " or Schedule: " + scheduleStr);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    public static boolean assignInstructorToOffering(int offeringId, int instructorId) {
        String updateSQL = "UPDATE offerings SET instructor_id = ? WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            stmt.setInt(1, instructorId);
            stmt.setInt(2, offeringId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning instructor to offering: " + e.getMessage());
            return false;
        }
    }

    public static List<Offering> getAllOfferings() {
        List<Offering> offerings = new ArrayList<>();
        String selectSQL = "SELECT id, location, schedule, lesson_type, is_available, instructor_id FROM offerings;";

        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int offeringId = rs.getInt("id");
                String locationStr = rs.getString("location");
                String scheduleStr = rs.getString("schedule");
                String lessonType = rs.getString("lesson_type");
                boolean isAvailable = rs.getBoolean("is_available");
                int instructorId = rs.getInt("instructor_id");

                Instructor instructor = null;
                if (!rs.wasNull()) {
                    instructor = userDAO.getInstructorById(instructorId).orElse(null);
                }
                try {
                    Location location = Location.fromString(locationStr);
                    Schedule schedule = Schedule.fromString(scheduleStr);
                    Offering offering = new Offering(offeringId, location, schedule, lessonType, isAvailable, instructor);
                    offerings.add(offering);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid location: " + locationStr + " or Schedule: " + scheduleStr);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }

        return offerings;
    }

    public void populateOfferings() {
        String[] insertSQLs = {
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id) VALUES ('Downtown, Montreal, QC', '10:00', 'Yoga', TRUE, NULL);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id) VALUES ('East Side, Gym, QC', '14:00', 'Pilates', TRUE, NULL);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id) VALUES ('West End, Center, ON', '09:00', 'Spinning', FALSE, NULL);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id) VALUES ('Loyola Campus, Montreal, QC', '08:00', 'Zumba', FALSE, NULL);"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : insertSQLs) {
                stmt.executeUpdate(sql);
            }
            System.out.println("Offerings table populated successfully.");
        } catch (SQLException e) {
            System.err.println("Error populating offerings table: " + e.getMessage());
        }
    }


    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }
}
