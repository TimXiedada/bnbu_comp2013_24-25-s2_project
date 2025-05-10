package net.xiedada.eventinfo.userutilities;
import java.util.ArrayList;
import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*; // Coming soon

public class EventOrganizer extends User{
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Customer> customersBlacklist = new ArrayList<Customer>();
    // Customers in these lists will not be able to book tickets for events from this EventOrganizer, as they are banned.
    public EventOrganizer(int userID, String username, String password) {
        super(userID ,username, password);
    } // Constructor for EventOrganizer class
    public void suspendUser(User user) throws IllegalArgumentException, BadStatusException {
        // Logic to suspend a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        else if (!(user instanceof Customer)){
            throw new IllegalArgumentException("Only Customers can be blacklisted from a EventOrganizer");
        }
        else {
            customersBlacklist.add((Customer) user); // Add the Customer to the blacklist
        }
    } // Method to suspend a user, only for admin use.

}
