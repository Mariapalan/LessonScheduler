package persistence;

import offerings.Location;
import offerings.Offering;
import offerings.Schedule;
import users.Client;
import users.Instructor;

import java.sql.*;
import java.util.*;

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
                + "max_size INTEGER, "
                + "FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE SET NULL"
                + ");";

        String createOfferingClientsTableSQL = "CREATE TABLE IF NOT EXISTS offering_clients ("
                + "offering_id INTEGER, "
                + "client_id INTEGER, "
                + "FOREIGN KEY (offering_id) REFERENCES offerings(id), "
                + "FOREIGN KEY (client_id) REFERENCES clients(id), "
                + "PRIMARY KEY (offering_id, client_id)"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createOfferingsTableSQL);
            stmt.execute(createOfferingClientsTableSQL);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables: " + e.getMessage(), e);
        }
    }

    public static boolean storeOffering(Offering offering) {
        String insertSQL = "INSERT INTO offerings (id, location, schedule, lesson_type, is_available, instructor_id, max_size) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
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
            stmt.setInt(7, offering.getMaxSize());  // Store max_size
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Optional<Offering> getOfferingById(int id) {
        String selectSQL = "SELECT id, location, schedule, lesson_type, is_available, instructor_id, max_size FROM offerings WHERE id = ?;";
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
                int maxSize = rs.getInt("max_size");

                List<Integer> clientIds = getClientsForOffering(offeringId);

                List<Client> clients = new ArrayList<>();
                Set<Integer> clientIdSet = new HashSet<>();
                for (int clientId : clientIds) {
                    Optional<Client> client = userDAO.getClientById(clientId);
                    client.ifPresent(c -> {
                        clientIdSet.add(clientId);
                        clients.add(c);
                    });
                }

                Instructor instructor = null;
                if (!rs.wasNull()) {
                    instructor = userDAO.getInstructorById(instructorId).orElse(null);
                }

                try {
                    Location location = Location.fromString(locationStr);
                    Schedule schedule = Schedule.fromString(scheduleStr);
                    Offering offering = new Offering(offeringId, location, schedule, lessonType, isAvailable, instructor, maxSize, clients, clientIdSet);
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
        String selectSQL = "SELECT id, location, schedule, lesson_type, is_available, instructor_id, max_size FROM offerings;";

        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int offeringId = rs.getInt("id");
                String locationStr = rs.getString("location");
                String scheduleStr = rs.getString("schedule");
                String lessonType = rs.getString("lesson_type");
                boolean isAvailable = rs.getBoolean("is_available");
                int instructorId = rs.getInt("instructor_id");
                int maxSize = rs.getInt("max_size");

                List<Integer> clientIds = getClientsForOffering(offeringId);

                List<Client> clients = new ArrayList<>();
                Set<Integer> clientIdSet = new HashSet<>();
                for (int clientId : clientIds) {
                    Optional<Client> client = userDAO.getClientById(clientId);
                    client.ifPresent(c -> {
                        clientIdSet.add(clientId);
                        clients.add(c);
                    });
                }


                Instructor instructor = null;
                if (!rs.wasNull()) {
                    instructor = userDAO.getInstructorById(instructorId).orElse(null);
                }
                try {
                    Location location = Location.fromString(locationStr);
                    Schedule schedule = Schedule.fromString(scheduleStr);
                    Offering offering = new Offering(offeringId, location, schedule, lessonType, isAvailable, instructor, maxSize, clients, clientIdSet);
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

    public static boolean addClientToOffering(int offeringId, int clientId) {
        String insertSQL = "INSERT INTO offering_clients (offering_id, client_id) VALUES (?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setInt(1, offeringId);
            stmt.setInt(2, clientId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Integer> getClientsForOffering(int offeringId) {
        List<Integer> clientIds = new ArrayList<>();
        String selectSQL = "SELECT client_id FROM offering_clients WHERE offering_id = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, offeringId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientIds.add(rs.getInt("client_id"));
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }

        return clientIds;
    }

    public void populateOfferings() {
        String[] insertSQLs = {
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id, max_size) VALUES ('Downtown, Montreal, QC', '10:00', 'Yoga', TRUE, NULL, 20);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id, max_size) VALUES ('East Side, Gym, QC', '14:00', 'Pilates', TRUE, NULL, 15);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id, max_size) VALUES ('West End, Center, ON', '09:00', 'Spinning', FALSE, NULL, 30);",
                "INSERT INTO offerings (location, schedule, lesson_type, is_available, instructor_id, max_size) VALUES ('Loyola Campus, Montreal, QC', '08:00', 'Zumba', FALSE, NULL, 25);"
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
