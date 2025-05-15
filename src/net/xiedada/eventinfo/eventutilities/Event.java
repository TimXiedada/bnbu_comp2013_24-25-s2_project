package net.xiedada.eventinfo.eventutilities;

import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.exceptions.*;
import java.time.*;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;

public class Event implements Serializable {
    private static ArrayList<Event> listOfAllEvents = new ArrayList<Event>();

    public static ArrayList<Event> getMyListOfEvents(EventOrganizer eventOrganizer) {
        ArrayList<Event> myEvents = new ArrayList<Event>();
        for (Event event : listOfAllEvents) {
            if (event.organizer_uid == eventOrganizer.userID) {
                myEvents.add(event);
            }
        }
        return myEvents;
    }

    public static enum EventStatus {
        PLANNING,
        APPLYING,
        APPROVED,
        DISAPPROVED,
        CANCELLED,
        AVAILABLE
    }

    public class EventData implements Cloneable {
        public String name;
        public String description;
        public String location;
        public LocalDate date;
        public int capacity;

        public EventData(String name, String description, String location, LocalDate date, int capacity) {
            this.name = name;
            this.description = description;
            this.location = location;
            this.date = date;
            this.capacity = capacity;
        }

        @Override
        public EventData clone() throws CloneNotSupportedException {
            return (EventData) super.clone();
        }
    }

    // 成员变量
    public final UUID eventID;
    public final int organizer_uid; // 组织者的用户ID
    public final char[] ticketTypes; // 票种类
    private EventOrganizer organizer;
    private EventData data;
    private EventStatus status;
    private ArrayList<Ticket> Tickets = new ArrayList<Ticket>();

    // 构造方法
    public Event(UUID eventID, String name, String description, String location, LocalDate date, int capacity,
            int organizer_uid, char[] ticketTypes) {
        this.eventID = eventID;
        this.data = new EventData(name, description, location, date, capacity);
        this.status = EventStatus.PLANNING;
        this.organizer_uid = organizer_uid;
        this.ticketTypes = ticketTypes;
    }

    // Getter 方法
    public UUID getID() {
        return eventID;
    }

    public EventStatus getStatus() {
        return status;
    }

    public EventData getInfo() {
        try {
            return data.clone();
        } catch (CloneNotSupportedException e) {
            // 此处不大可能发生，因为 EventData 已实现 Cloneable 接口
            return null;
        }
    }

    public EventOrganizer getOrganizer() {
        if (organizer != null) {
            return organizer;
        } else {
            User user = User.findUserWithUID(this.organizer_uid);
            if (!(user instanceof EventOrganizer)) {
                throw new IllegalArgumentException("The user with the given UID is not an EventOrganizer");
            }
            organizer = (EventOrganizer) user; // Cache the reference
            return organizer;
        }

    }

    // 业务逻辑方法
    public boolean approved() {
        return status == EventStatus.APPROVED || status == EventStatus.AVAILABLE;
    }

    public void approve() throws BadStatusException {
        if (status != EventStatus.APPLYING) {
            throw new BadStatusException("Event is already approved");
        }
        status = EventStatus.APPROVED;
    }

    public void disapprove() throws BadStatusException {
        if (status != EventStatus.APPLYING) {
            throw new BadStatusException("Event is already disapproved");
        }
        status = EventStatus.DISAPPROVED;
    }

    public void cancel() throws BadStatusException {
        if (status != EventStatus.APPROVED && status != EventStatus.AVAILABLE && status != EventStatus.APPLYING) {
            throw new BadStatusException("Event cannot be cancelled as it is not in a cancellable state");
        }
        status = EventStatus.CANCELLED;
    }

    public void updateEventData(EventData newData) throws IllegalArgumentException, CloneNotSupportedException {
        if (newData == null) {
            throw new IllegalArgumentException("Event data cannot be null");
        }
        if (newData.name == null || newData.description == null || newData.location == null || newData.date == null
                || newData.capacity <= 0) {
            throw new IllegalArgumentException("Invalid event data");
        }
        this.data = newData.clone();

        status = EventStatus.PLANNING; // Reset status to PLANNING after updating data

    }

    public void setAvailable() throws BadStatusException {
        if (status != EventStatus.APPROVED) {
            throw new BadStatusException("Event is not approved to be made available");
        }
        status = EventStatus.AVAILABLE;
    }
    // public void addAttendee(Customer c) throws IllegalArgumentException {
    // if (c == null) {
    // throw new IllegalArgumentException("Customer cannot be null");
    // }
    // listOfAllAttendees.add(c);

    // }

    public Ticket createTicket(Customer customer, char ticketType) throws IllegalArgumentException {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        } else if (this.ticketTypes == null || !new String(this.ticketTypes).contains(String.valueOf(ticketType))) {
            throw new IllegalArgumentException("Invalid ticket type");
        } else if (Tickets.size()>this.data.capacity){
            return null;
        }
        this.Tickets.add(new Ticket(this, customer, ticketType));
        return this.Tickets.get(this.Tickets.size() - 1);
    }

    public void changeTicket(Ticket ticket, char newType) throws IllegalArgumentException {
        if (ticket == null || !Tickets.contains(ticket)) {
            throw new IllegalArgumentException("Ticket is invalid");
        } else if (ticketTypes == null || !new String(ticketTypes).contains(String.valueOf(newType))) {
            throw new IllegalArgumentException("Invalid ticket type");
        } else {
            ticket.setType(newType);
        }
    }

    public void ReturnTicket(Ticket ticket) {

        if (ticket == null || !Tickets.contains(ticket)) {
            throw new IllegalArgumentException("Ticket is null or not found in the list of tickets");
        }
        Tickets.remove(ticket);
    }

}
