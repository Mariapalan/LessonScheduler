package persistence;

import users.Admin;
import users.Client;
import users.Instructor;

import java.sql.*;
import java.util.Optional;

public class UserDAO {

    private Connection connection;

    public UserDAO(String dbPath) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            this.connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the SQLite database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        initializeDatabase();
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

    private void initializeDatabase() {
        String createAdminTableSQL = """
                    CREATE TABLE IF NOT EXISTS admins (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        phone TEXT NOT NULL
                    );
                """;

        String createClientTableSQL = """
                    CREATE TABLE IF NOT EXISTS clients (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        age INTEGER NOT NULL
                    );
                """;
        String createInstructorTableSQL = """
                    CREATE TABLE IF NOT EXISTS  instructors (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        phone TEXT NOT NULL,
                        age INTEGER NOT NULL,
                        specialization TEXT
                    );
                """;
        try (PreparedStatement adminStmt = connection.prepareStatement(createAdminTableSQL); PreparedStatement clientStmt = connection.prepareStatement(createClientTableSQL); PreparedStatement instrStmt = connection.prepareStatement(createInstructorTableSQL)) {
            adminStmt.execute();
            clientStmt.execute();
            instrStmt.execute();
            System.out.println("Tables for Admins, Instructors, and Clients are ready.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void insertAdmin(Admin admin) {
        String insertSQL = "INSERT INTO admins (name, phone) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getPhone());
            pstmt.executeUpdate();
            System.out.println("Admin inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting admin: " + e.getMessage());
        }
    }

    public void insertClient(Client client) {
        String insertSQL = "INSERT INTO clients (name, phone, age) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getPhone());
            pstmt.setInt(3, client.getAge());
            pstmt.executeUpdate();
            System.out.println("Client inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting client: " + e.getMessage());
        }
    }

    public Optional<Client> getClientById(int clientId) {
        String selectSQL = "SELECT id, name, phone, age FROM clients WHERE id = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                return Optional.of(new Client(id, name, phone, age));
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Admin> getAdminById(int adminID) {
        String selectSQL = "SELECT id, name, phone FROM admins WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, adminID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                return Optional.of(new Admin(id, name, phone));
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Instructor> getInstructorById(int instructorID) {
        String selectSQL = "SELECT id, name, phone, specialization FROM instructors WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, instructorID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String specialization = rs.getString("specialization");
                return Optional.of(new Instructor(id, name, phone, specialization, "Montreal, QC"));
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Client> getClientById(String clientID) {
        return Optional.empty();
    }
}
