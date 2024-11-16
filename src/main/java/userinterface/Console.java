package userinterface;

import offerings.Location;
import offerings.Offering;
import offerings.OfferingCatalog;
import offerings.Schedule;
import persistence.OfferingDAO;
import persistence.UserDAO;
import users.Admin;
import users.Client;
import users.Instructor;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Console {

    private final UserDAO userDAO;
    private final OfferingDAO offeringDAO;
    private final OfferingCatalog offeringCatalog;

    public Console(UserDAO userDAO, OfferingDAO offeringDAO) {
        this.userDAO = userDAO;
        this.offeringDAO = offeringDAO;
        this.offeringCatalog = new OfferingCatalog(offeringDAO);
    }

    public void beginSession() {
        try (Scanner scanner = new Scanner(System.in)) {
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
                        try {
                            int adminId = scanner.nextInt();
                            scanner.nextLine();
                            Optional<Admin> adminOpt = userDAO.getAdminById(adminId);
                            adminOpt.ifPresentOrElse(admin -> processOfferingsAdmin(admin, scanner), () -> System.out.println("Admin not found"));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Admin ID, please enter a valid Integer");
                        }
                    }
                    case "2" -> {
                        System.out.print("Enter Instructor ID: ");
                        int instructorId = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Instructor> instructorOpt = userDAO.getInstructorById(instructorId);
                        instructorOpt.ifPresentOrElse(instructor -> processOfferingsInstructor(instructor, scanner), () -> System.out.println("Instructor not found"));
                    }
                    case "3" -> {
                        System.out.print("Enter Client ID: ");
                        int clientId = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Client> clientOpt = userDAO.getClientById(clientId);
                        clientOpt.ifPresentOrElse(client -> processOfferingsClient(client, scanner), () -> System.out.println("Client not found"));
                    }
                    case "4" -> {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    default ->
                        System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                }
            }
        }
    }

    private void processOfferingsAdmin(Admin admin, Scanner scanner) {
        System.out.println("Processing offerings for Admin: " + admin.getName());
        System.out.println("Enter offering Lesson:");
        String lesson = scanner.nextLine();
        System.out.println("Enter offering Location:");
        var location = Location.fromString(scanner.nextLine()); // example: Downtown, Montreal, QC

        System.out.println("Enter offering Schedule:");
        var schedule = Schedule.fromString(scanner.nextLine()); // example: 10:15

        OfferingCatalog offeringCatalog = new OfferingCatalog(offeringDAO);
        if (offeringCatalog.isOfferingUnique(location, schedule)) {
            var offeringOpt = offeringCatalog.createUniqueOffering(location, schedule, lesson);
            offeringOpt.ifPresent(offeringCatalog::add);
            System.out.println("Successfully Added an Offering for Lesson: " + lesson + " at Location: " + location + ", at time: " + schedule);
        } else {
            System.out.println("Operation failed, Offering Location or Schedule not unique");
        }
    }

    private void processOfferingsInstructor(Instructor instructor, Scanner scanner) {
        System.out.println("Processing offerings for Instructor: " + instructor.getName());
        System.out.println('\n');

        List<Offering> unassignedOfferings = offeringCatalog.getAllUnassignedOfferings();

        if (unassignedOfferings.isEmpty()) {
            System.out.println("No unassigned offerings available.");
            return;
        }

        System.out.println("\nAvailable unassigned offerings:\n");
        unassignedOfferings.forEach(offering
                -> System.out.println("ID: " + offering.getId() + ", Location: " + offering.getLocation() + ", Schedule: " + offering.getSchedule())
        );

        System.out.print("Enter the ID of the offering you want to select: ");
        int selectedId = scanner.nextInt();
        scanner.nextLine();

        Optional<Offering> selectedOffering = offeringCatalog.selectOffering(selectedId, instructor);

        if (selectedOffering.isPresent()) {
            System.out.println(selectedOffering.get() + " has successfully been assigned to " + instructor.getName());
        } else {
            System.out.println("Invalid offering ID or the offering has already been assigned.");
        }
    }

    private void processOfferingsClient(Client client, Scanner scanner) {
        System.out.println("Processing offerings for Client: " + client.getName());
        List<Offering> publicOfferings = offeringCatalog.getAllPublicOfferings();

        if (publicOfferings.isEmpty()) {
            System.out.println("No Available offerings at this time.");
            return;
        }

        System.out.println("Available offerings:");
        publicOfferings.forEach(offering
                -> {
            if (offering.getClientIdSet().contains(client.getId())) {
                System.out.println("[Already Registered] ID: " + offering.getId() + ", Location: " + offering.getLocation() + ", Schedule: " + offering.getSchedule());
            }
            System.out.println("ID: " + offering.getId() + ", Location: " + offering.getLocation() + ", Schedule: " + offering.getSchedule());
        }
        );

        System.out.print("Enter the ID of the offering you want to select: ");
        int selectedId = scanner.nextInt();
        scanner.nextLine();

        if (offeringCatalog.selectOffering(selectedId, client)) {
            System.out.println("Offering successfully assigned to " + client.getName());
        } else {
            System.out.println("Invalid offering ID or the offering has already reached maximum capacity.");
        }
    }

    public static void main(String[] args) {
        String dbName = "sample.db";

        UserDAO userDAO = new UserDAO(dbName);
        OfferingDAO offeringDAO = new OfferingDAO(dbName, userDAO);

        Console console = new Console(userDAO, offeringDAO);
        console.beginSession();

        userDAO.close();
        offeringDAO.close();
    }
}
