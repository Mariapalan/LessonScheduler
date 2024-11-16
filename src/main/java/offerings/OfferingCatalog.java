package offerings;

import persistence.OfferingDAO;
import users.Client;
import users.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OfferingCatalog {

    private List<Offering> offerings;

    public OfferingCatalog(OfferingDAO offeringDAO) {
        offerings = new ArrayList<>(OfferingDAO.getAllOfferings());
    }

    // Check if offering is unique based on location and schedule
    public boolean isOfferingUnique(Location location, Schedule schedule) {
        for (Offering offering : offerings) {
            if (offering.getLocation().equals(location) && offering.getSchedule().equals(schedule)) {
                return false;
            }
        }
        return true;
    }

    // Create a new offering if it is unique
    public Optional<Offering> createUniqueOffering(Location location, Schedule schedule, String lessonType) {
        if (isOfferingUnique(location, schedule)) {
            Offering newOffering = new Offering(location, schedule, lessonType);
            add(newOffering);
            return Optional.of(newOffering);
        }
        return Optional.empty();
    }

    // Add a new offering to the catalog and store in DB
    public void add(Offering offering) {
        if (isOfferingUnique(offering.getLocation(), offering.getSchedule()) && !offerings.contains(offering) && OfferingDAO.storeOffering(offering)) {
            offerings.add(offering);
        }
    }

    public List<Offering> getAllUnassignedOfferings() {
        return offerings.stream().filter(offering -> !offering.hasInstructor()).collect(Collectors.toList());
    }

    public List<Offering> getAllPublicOfferings() {
        return offerings.stream().filter(Offering::isAvailableToPublic).collect(Collectors.toList());
    }

    // Select an offering by ID and assign an instructor
    public Optional<Offering> selectOffering(int offeringId, Instructor instructor) {
        for (Offering offering : offerings) {
            if (offering.getId() == offeringId) {
                if (offering.assignInstructor(instructor) && OfferingDAO.assignInstructorToOffering(offeringId, instructor.getId())) {
                    return Optional.of(offering);
                }
            }
        }
        return Optional.empty();
    }

    public boolean selectOffering(int offeringId, Client client) {
        for (Offering offering : offerings) {
            if (offering.getId() == offeringId) {
                if (offering.registerClient(client) && OfferingDAO.addClientToOffering(offeringId, client.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
