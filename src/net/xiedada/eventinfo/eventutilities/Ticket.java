package net.xiedada.eventinfo.eventutilities;
import net.xiedada.eventinfo.userutilities.*;
// import net.xiedada.eventinfo.exceptions.*;
import java.io.Serializable;
import java.util.UUID;

public class Ticket implements Serializable {
    // private int ticketID;
    public final UUID ticketID;
    private Event event;
    private Customer owner;
    private char type;
    public Ticket(UUID ticketID, Event event, Customer owner, char type) {
        this.ticketID = ticketID;
        this.event = event;
        this.owner = owner;
        this.type = type;
    }
    public Ticket(Event event, Customer owner, char type) {
        this.ticketID = UUID.randomUUID();
        this.event = event;
        this.owner = owner;
        this.type = type;
    }
    // Getter methods
    public UUID getTicketID() {
        return ticketID;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public Customer getOwner() {
        return owner;
    }
    
    public char getType() {
        return type;
    }
    // Setter methods
}
