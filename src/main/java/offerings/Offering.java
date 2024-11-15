package offerings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import users.Client;
import users.Instructor;

public class Offering {

    private boolean isAvailableToPublic;
    private boolean hasInstructor;
    private final Location location;
    private final Schedule schedule;
    private final String lessonType;
    private Instructor instructor;
    private final int id;
    private final List<Client> clients;
    private int size = 3; // placeholder value

    public Offering(Location location, Schedule schedule, String lessonType) {
        this(generateUniqueOfferingId(), location, schedule, lessonType, false, null);
    }

    public Offering(int offeringId, Location location, Schedule schedule, String lessonType, boolean isAvailableToPublic, Instructor instructor) {
        this.clients = new ArrayList<>();
        this.isAvailableToPublic = isAvailableToPublic;
        this.location = location;
        this.schedule = schedule;
        this.lessonType = lessonType;
        this.id = offeringId;
        hasInstructor = instructor != null;
        this.instructor = instructor;
    }

    public void updateAvailability(boolean availability) {
        this.isAvailableToPublic = availability;
    }

    public boolean hasInstructor() {
        return hasInstructor;
    }

    public int getId() {
        return id;
    }

    // Method to check availability
    public boolean checkAvailable() {
        return isAvailableToPublic;
    }

    public boolean assignInstructor(Instructor instructor) {
        if (!hasInstructor) {
            this.instructor = instructor;
            this.hasInstructor = true;
            this.isAvailableToPublic = true;
            return true;
        }
        return false;
    }

    public boolean registerClient(Client client) {
        if (!clients.contains(client) && clients.size() < size) {
            clients.add(client);
            if (clients.size() >= size) {
                isAvailableToPublic = false;
            }

            return true;
        }
        return false;
    }

    // Getter for location
    public Location getLocation() {
        return location;
    }

    // Getter for schedule
    public Schedule getSchedule() {
        return schedule;
    }

    // Getter for instructor
    public Instructor getInstructor() {
        return instructor;
    }

    // Getter for lessonType
    public String getLessonType() {
        return lessonType;
    }

    public boolean isAvailableToPublic() {
        return isAvailableToPublic;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Offering) {
            Offering otherOffering = (Offering) other;
            return this.location.equals(otherOffering.location)
                    && this.schedule.equals(otherOffering.schedule);
        }
        return false;
    }

    public String toString() {
        return "Offering for Lesson: " + lessonType + " at Location: " + location + ", at time: " + schedule;
    }

    // Generate a unique offering ID
    private static int generateUniqueOfferingId() {
        return UUID.randomUUID().toString().hashCode();
    }
}
