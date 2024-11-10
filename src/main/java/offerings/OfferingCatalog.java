package offerings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import users.Instructor;

public class OfferingCatalog {

    private static List<Offering> offerings = new ArrayList<>();

    // Check if offering is unique based on location and schedule
    public static boolean isOfferingUnique(Location location, Schedule schedule) {
        for (Offering offering : offerings) {
            if (offering.getLocation().equals(location) && offering.getSchedule().equals(schedule)) {
                return false;
            }
        }
        return true;
    }

    // Create a new offering if it is unique
    public static Offering createOffering(Location location, Schedule schedule, String lessonType) {
        if (isOfferingUnique(location, schedule)) {
            Offering newOffering = new Offering(location, schedule, lessonType);
            add(newOffering);
            return newOffering;
        }
        return null;
    }

    // Add a new offering to the catalog
    public static void add(Offering offering) {
        offerings.add(offering);
    }

    // Get all unassigned offerings
    public static List<Offering> getAllUnassignedOfferings() {
        List<Offering> unassignedOfferings = new ArrayList<>();
        for (Offering offering : offerings) {
            if (offering.getInstructor() == null) {
                unassignedOfferings.add(offering);
            }
        }
        return unassignedOfferings;
    }

    // Select an offering by ID and assign an instructor
    public static Optional<Offering> selectOffering(String offeringId, Instructor instructor) {
        for (Offering offering : offerings) {
            if (offering.getId().equals(offeringId)) {
                if (offering.assignInstructor(instructor)) {
                    return Optional.of(offering);
                }
            }
        }
        return Optional.empty();
    }
}
