package offerings;

import java.time.LocalTime;

public class Schedule {

    private LocalTime timeslot;

    // Constructor
    public Schedule(LocalTime timeslot) {
        this.timeslot = timeslot;
    }

    // Getter for timeslot
    public LocalTime getTimeslot() {
        return timeslot;
    }

    // Setter for timeslot
    public void setTimeslot(LocalTime timeslot) {
        this.timeslot = timeslot;
    }

    public static Schedule fromString(String timeslotString) throws IllegalArgumentException {
        try {
            LocalTime timeslot = LocalTime.parse(timeslotString.trim());
            return new Schedule(timeslot);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timeslot format. Expected 'HH:mm' or 'HH:mm:ss'", e);
        }
    }

    @Override
    public String toString() {
        return timeslot.toString();
    }

    public boolean equals(Object other) {
        if (other instanceof Schedule) {
            return timeslot.equals(((Schedule) other).timeslot);
        }
        return false;
    }
}
