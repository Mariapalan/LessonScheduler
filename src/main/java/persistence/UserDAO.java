package persistence;

import users.Admin;
import users.Client;
import users.Instructor;

import java.util.Optional;

public class UserDAO {
    public Optional<Admin> getAdminById(String adminID) {
        return Optional.empty();
    }

    public Optional<Instructor> getInstructorById(String instructorID) {
        return Optional.empty();
    }

    public Optional<Client> getClientById(String clientID) {
        return Optional.empty();
    }
}
