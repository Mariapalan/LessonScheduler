package users;

public class Client extends Person {

    private int age;

    public Client(String id, String name, float phone, int age) {
        super(id, name, phone);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
