package users;

import offerings.Offering;

public class Admin {

    private String name;
    private String id;

    // Constructor
    public Admin(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Example method using the Offering class
    public void createOffering(Offering offering) {
        // Logic to create or handle the offering
        System.out.println("Creating offering: " + offering.getLessonType());
    }
}
