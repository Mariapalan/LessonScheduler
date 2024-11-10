package offerings;

import java.util.Date;
import java.util.UUID;
import users.Client;

public class Booking {

    private String bookingId;
    private Client client;
    private Offering offering;
    private Date date;
    private boolean status;
    private Date time;
    private Client guardian;
    private String timeSlot;

    // Constructor
    public Booking(Client client, Offering offering) {
        this.bookingId = UUID.randomUUID().toString();
        this.client = client;
        this.offering = offering;
        this.date = new Date();
        this.status = true;
        this.time = new Date();  // Current date and time
        this.guardian = null;
        this.timeSlot = null;
    }

    // Method to make a booking
    public boolean makeBooking(Client client, Offering offering, String timeSlot) {
        if (offering.checkAvailable()) {
            this.client = client;
            this.offering = offering;
            this.timeSlot = timeSlot;
            this.status = true;
            offering.updateAvailability(false);
            return true;
        } else {
            System.out.println("Offering is not available.");
            return false;
        }
    }

    // Method to cancel a booking
    public boolean cancelBooking() {
        if (this.status) {
            this.status = false;
            this.offering.updateAvailability(true);
            return true;
        } else {
            System.out.println("Booking is already canceled.");
            return false;
        }
    }

    // Method to check if the booking is active
    public boolean isAvailable() {
        return this.status;
    }

    // Method to get booking details
    public String getBookingDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Booking ID: ").append(bookingId).append("\n")
                .append("Client Name: ").append(client.getName()).append("\n")
                .append("Client ID: ").append(client.getId()).append("\n")
                .append("Offering Location: ").append(offering.getLocation()).append("\n")
                .append("Lesson Type: ").append(offering.getLessonType()).append("\n")
                .append("Date: ").append(date).append("\n")
                .append("Time Slot: ").append(timeSlot).append("\n")
                .append("Status: ").append(status ? "Active" : "Canceled");

        if (guardian != null) {
            details.append("\nGuardian: ").append(guardian.getName())
                    .append(" (ID: ").append(guardian.getId()).append(")");
        }

        return details.toString();
    }

    // Method to update the booking status
    public void updateStatus(boolean newStatus) {
        this.status = newStatus;
    }

    // Method to set a guardian for underage clients
    public void setGuardian(Client guardian) {
        this.guardian = guardian;
    }

    // Method to change the time slot of the booking
    public void changeTimeSlot(String newTimeSlot) {
        this.timeSlot = newTimeSlot;
    }
}
