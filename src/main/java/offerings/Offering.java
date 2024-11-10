package offerings;

import java.util.UUID;
import users.Instructor;

public class Offering {

    private boolean isAvailable;
    private Location location;
    private Schedule schedule;
    private String lessonType;
    private Instructor instructor;
    private String id;

    // Constructor
    public Offering(Location location, Schedule schedule, String lessonType) {
        this.isAvailable = false;
        this.location = location;
        this.schedule = schedule;
        this.lessonType = lessonType;
        this.instructor = null;
        this.id = generateUniqueOfferingId();
    }

    // Method to update availability
    public void updateAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    // Method to check equality based on location and schedule
    @Override
    public boolean equals(Object other) {
        if (other instanceof Offering) {
            Offering otherOffering = (Offering) other;
            return this.location.equals(otherOffering.location)
                    && this.schedule.equals(otherOffering.schedule);
        }
        return false;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Method to check availability
    public boolean checkAvailable() {
        return isAvailable;
    }

    // Method to assign an instructor
    public boolean assignInstructor(Instructor instructor) {
        if (checkAvailable()) {
            this.instructor = instructor;
            this.isAvailable = false;
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

    // Override hashCode to use id
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // Generate a unique offering ID
    private static String generateUniqueOfferingId() {
        return UUID.randomUUID().toString();
    }
}
