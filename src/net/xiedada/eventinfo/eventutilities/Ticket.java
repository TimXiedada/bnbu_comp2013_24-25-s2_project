package net.xiedada.eventinfo.eventutilities;
import net.xiedada.eventinfo.userutilities.*;
// import net.xiedada.eventinfo.exceptions.*;
import java.io.Serializable;
import java.util.UUID;

public class Ticket implements Serializable {
    // private int ticketID;
    public final UUID ticketID;
    private final Event event;
    private final Customer owner;
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
    public void setType(char type) {
        this.type = type;
    }

    // public void ChangeTicket(char newType) throws IllegalArgumentException{
    //     if (event.ticketTypes == null || !new String(event.ticketTypes).contains(String.valueOf(type))){
    //         throw new IllegalArgumentException("Invalid ticket type");
    //     }else{
    //         this.setType(newType);
    //     }
    // }
}
