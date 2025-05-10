package net.xiedada.eventinfo.userutilities;
import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*;

public class Administrator extends User{
    public Administrator(int uid, String username, String password) {
        super(uid, username, password);
    }
    public void suspendUser(User user) throws IllegalArgumentException, BadStatusException {
        // Logic to suspend a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.isSignedIn()) {
            user.logout(); // Log out the user if they are currently signed in
        }
        user.locked = true; // Set the locked status to true
    }
    public void approveEvent(Event event) throws IllegalArgumentException, BadStatusException {
        // Logic to approve an event
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.approved()) {
            throw new BadStatusException("Event is already approved");
        }
        event.approve(); // Approve the event
    }
    public void disapproveEvent(Event event) throws IllegalArgumentException {
        // Logic to disapprove an event
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        event.disapprove(); // Disapprove the event
    }



}
