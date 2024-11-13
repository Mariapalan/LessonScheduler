package userinterface;

import offerings.Location;
import offerings.OfferingCatalog;
import offerings.Schedule;
import persistence.OfferingDAO;
import persistence.UserDAO;
import users.Admin;
import users.Client;
import users.Instructor;

import java.sql.*;
import java.util.Optional;
import java.util.Scanner;

public class Console {
    private final OfferingDAO offeringDAO;
    private final UserDAO userDAO;

    public Console(OfferingDAO offeringDAO, UserDAO userDAO) {
        this.offeringDAO = offeringDAO;
        this.userDAO = userDAO;
    }

    // todo: add missing fnuctionality (ex: admin needs to add stuff, user needs to sign up)
    public void beginSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the application!");

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Log in as Admin");
            System.out.println("2. Log in as Instructor");
            System.out.println("3. Log in as Client");
            System.out.println("4. Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter Admin ID: ");
                    String adminId = scanner.nextLine();
                    Optional<Admin> adminOpt = userDAO.getAdminById(adminId);
                    adminOpt.ifPresentOrElse(this::processOfferingsAdmin, () -> System.out.println("Admin not found"));
                }
                case "2" -> {
                    System.out.print("Enter Instructor ID: ");
                    String instructorId = scanner.nextLine();
                    Optional<Instructor> instructorOpt = userDAO.getInstructorById(instructorId);
                    instructorOpt.ifPresentOrElse(this::processOfferingsInstructor, () -> System.out.println("Instructor not found"));
                }
                case "3" -> {
                    System.out.print("Enter Client ID: ");
                    String clientId = scanner.nextLine();
                    Optional<Client> clientOpt = userDAO.getClientById(clientId);
                    clientOpt.ifPresentOrElse(this::processOfferingsClient, () -> System.out.println("Client not found"));
                }
                case "4" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
            }
        }
    }

    private void processOfferingsAdmin(Admin admin) {
        System.out.println("Processing offerings for Admin: " + admin.getName());
    }

    private void processOfferingsInstructor(Instructor instructor) {
        System.out.println("Processing offerings for Instructor: " + instructor.getName());
    }

    private void processOfferingsClient(Client client) {
        System.out.println("Processing offerings for Instructor: " + client.getName());
    }

    private void makeNewOffering(Location location, Schedule schedule, String lessonType) {
        var offeringOpt = OfferingCatalog.createOffering(location, schedule, lessonType);
        if (offeringOpt.isPresent() && offeringDAO.store(offeringOpt.get())) {
            System.out.println("New offering created successfully!");
        } else {
            System.out.println("Offering already exists.");
        }
    }


    public static void main(String[] args) {
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
