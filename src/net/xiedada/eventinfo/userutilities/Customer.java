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

import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*; // Coming soon
import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    // Events from these EventOrganizers in this list will be hidden from the
    // customer, as user stated that they have no interest in them.
    private ArrayList<EventOrganizer> eventOrganizersBlacklist = new ArrayList<EventOrganizer>();

    public Customer(String username, String password) {
        super(username, password, User.UserType.CUSTOMER);
    } // Constructor for Customer class

    public ArrayList<Ticket> getTickets() {
        return tickets;
    } // Getter for tickets

    public void suspendUser(User user) throws IllegalArgumentException, BadStatusException {
        // Logic to suspend a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        } else if (!(user instanceof EventOrganizer)) {
            throw new IllegalArgumentException("Only EventOrganizers can be suspended");
        } else {
            eventOrganizersBlacklist.add((EventOrganizer) user); // Add the EventOrganizer to the blacklist
        }
    } // Method to suspend a user, only for admin use.

    public boolean isBlacklisted(EventOrganizer user) throws IllegalArgumentException {
        // Logic to check if a user is blacklisted
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        } else if (eventOrganizersBlacklist.contains(user)) {
            return true; // User is blacklisted
        } else
            return false; // User is not blacklisted
    } // Method to check if a user is blacklisted, only for admin use.

    public void bookTicket(Event event, char type)
            throws IllegalArgumentException, BadStatusException, NotPermittedException {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        EventOrganizer organizer = event.getOrganizer();
        if (event.getStatus() != Event.EventStatus.AVAILABLE) {
            throw new BadStatusException("Event is not available for booking");
        } else {
            if (!this.isSignedIn()) {
                throw new NotPermittedException("User must be signed in to book a ticket");
            }
            Ticket ticket = organizer.orderTicket(this, event, 'A');
            tickets.add(ticket);
        }
    } // Method to book a ticket for an Event

    public void ChangeTicket(Ticket ticket,char newType) throws Throwable {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        } else if (tickets.stream().noneMatch(t -> t.getTicketID().equals(ticket.getTicketID()))) {
            throw new IllegalArgumentException("One cannot dispose of what does not belong to them");
        } else {
            ticket.getEvent().changeTicket(ticket, newType);
            tickets.removeIf(t -> t.getTicketID().equals(ticket.getTicketID()));
            tickets.add(ticket);
        }
    }
    public void ReturnTicket(Ticket ticket) throws IllegalArgumentException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        } else if (tickets.stream().noneMatch(t -> t.getTicketID().equals(ticket.getTicketID()))) {
            throw new IllegalArgumentException("One cannot dispose of what does not belong to them");
        } else {
            tickets.removeIf(t -> t.getTicketID().equals(ticket.getTicketID()));
            ticket.getEvent().ReturnTicket(ticket);
        }
    } // Method to return a ticket for an Event
    
}
