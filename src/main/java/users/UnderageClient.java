package users;

public class UnderageClient extends Client {

    private Client guardian;

    // Constructor
    public UnderageClient(int id, String name, String phone, int age, Client guardian) {
        super(id, name, phone, age);
        this.guardian = guardian;
    }

    // Getter for guardian
    public Client getGuardian() {
        return guardian;
    }

    // Setter for guardian
    public void setGuardian(Client guardian) {
        this.guardian = guardian;
    }
}
