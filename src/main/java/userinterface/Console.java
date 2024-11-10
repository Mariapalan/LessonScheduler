package userinterface;

import offerings.Location;
import offerings.Offering;
import offerings.OfferingCatalog;
import offerings.Schedule;
import users.Admin;

public class Console {

    // Constructor
    public Console() {
        // Empty constructor
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

    // Additional methods (commented out) for instructors and clients
    // Uncomment and implement as needed
    /*
    public void processOfferingsInstructor(Instructor instructor) {
        System.out.println("Processing offerings for Instructor: " + instructor.getName());
    }

    public void processOfferingsClient(Client client) {
        System.out.println("Processing offerings for Client: " + client.getName());
    }
     */
    // Main method for testing
    public static void main(String[] args) {
        Console console = new Console();
    }
}
