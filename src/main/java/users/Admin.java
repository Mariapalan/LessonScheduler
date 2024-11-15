package users;

import offerings.Offering;

public class Admin extends Person {


    public Admin(int id, String name, String phone) {
        super(id, name, phone);
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
