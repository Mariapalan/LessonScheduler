package users;

public class Instructor extends Person {

    private String specialization;
    private String city;

    // Constructor
    public Instructor(String id, String name, String phone, String specialization, String city) {
        super(id, name, phone);
        this.specialization = specialization;
        this.city = city;
    }

    // Getter for specialization
    public String getSpecialization() {
        return specialization;
    }

    // Setter for specialization
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Getter for city
    public String getCity() {
        return city;
    }

    // Setter for city
    public void setCity(String city) {
        this.city = city;
    }
}
