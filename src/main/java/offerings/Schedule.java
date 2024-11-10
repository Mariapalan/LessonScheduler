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
}
