/* SPDX-License-Identifier: Apache-2.0 */
/*
    Copyright (c) 2024 Xie Youtian. 

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
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

    public ArrayList<Event> getEvents() {
        return events;
    } // Getter for events

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

    public void createEvent(String name, String description, String location, LocalDate date, int capacity, char[] ticketTypes)
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
        // Check if the event with the same UUID exists in this organizer's events
        boolean found = false;
        for (Event e : events) {
            if (e.getID().equals(event.getID())) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new BadStatusException("Event does not belong to this organizer");
        } else {
            return event.createTicket(customer, ticketType);
        }
    }
}
