package users;

public class Client extends Person {

    private int age;

    // Constructor
    public Client(String id, String name, float phone, int age) {
        super(id, name, phone);
        this.age = age;
    }

    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }
}
