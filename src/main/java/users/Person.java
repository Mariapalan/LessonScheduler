package users;

public abstract class Person {

    private String id;
    private String name;
    private float phone;

    // Constructor
    public Person(String id, String name, float phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPhone() {
        return phone;
    }

    public void setPhone(float phone) {
        this.phone = phone;
    }
}