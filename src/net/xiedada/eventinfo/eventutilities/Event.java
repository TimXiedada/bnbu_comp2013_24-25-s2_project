package net.xiedada.eventinfo.eventutilities;
import net.xiedada.eventinfo.userutilities.*;
import java.time.*;
public class Event {
    private class EventInfo {
        String name;
        String location;
        LocalDate date;
        int capacity;
        EventInfo(String name, String location, LocalDate date, int capacity) {
            this.name = name;
            this.location = location;
            this.date = date;
            this.capacity = capacity;
        }
    }
    private EventInfo eventInfo = EventInfo("Default Event", "Default Location", LocalDate.now(), 100);

}
