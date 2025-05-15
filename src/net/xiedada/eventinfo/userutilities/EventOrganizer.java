package net.xiedada.eventinfo.userutilities;

import java.util.ArrayList;
import java.util.UUID;

import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*; // Coming soon
import java.time.*;

public class EventOrganizer extends User {
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Customer> customersBlacklist = new ArrayList<Customer>();

    // Customers in these lists will not be able to book tickets for events from
    // this EventOrganizer, as they are banned.
    public EventOrganizer(String username, String password) {
        super(username, password, User.UserType.EVENT_ORGANIZER);
        this.events = Event.getMyListOfEvents(this);
    } // Constructor for EventOrganizer class

    public void suspendUser(User user) throws IllegalArgumentException, BadStatusException {
        // Logic to suspend a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        } else if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("Only Customers can be blacklisted by a EventOrganizer");
        } else {
            customersBlacklist.add((Customer) user); // Add the Customer to the blacklist
        }
    }

    void createEvent(String name, String description, String location, LocalDate date, int capacity, char[] ticketTypes)
            throws IllegalArgumentException {
        // Logic to create an event
        if (name == null || description == null || location == null || date == null || capacity <= 0
                || ticketTypes.length == 0) {
            throw new IllegalArgumentException("Invalid event data");
        } else {
            Event event = new Event(UUID.randomUUID(), name, description, location, date, capacity, this.userID,
                    ticketTypes); // Create a new event
            events.add(event); // Add the event to the list of events
        }
    } // Method to create an event

    public Ticket orderTicket(Customer customer, Event event, char ticketType) throws BadStatusException,NotPermittedException {
        if (customersBlacklist.contains(customer)){
            throw new NotPermittedException("You have been blacklisted!");
        }
        else if (event.getStatus() != Event.EventStatus.AVAILABLE){
            throw new BadStatusException("Event is not available for booking");
        }
        else if (!events.contains(event)){
            throw new BadStatusException("Event does not belong to this organizer");
        }
        else {
            return event.createTicket(customer, ticketType);
        }
    }
}
