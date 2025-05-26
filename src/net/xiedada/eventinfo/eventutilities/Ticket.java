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
package net.xiedada.eventinfo.eventutilities;
import net.xiedada.eventinfo.userutilities.*;
import java.util.HashMap;
// import net.xiedada.eventinfo.exceptions.*;
import java.io.Serializable;
import java.util.UUID;

public class Ticket implements Serializable {
    private static HashMap<UUID, Ticket> ListOfAllTickets = new HashMap<>();
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
        ListOfAllTickets.put(this.ticketID, this);
    }
    public Ticket(Event event, Customer owner, char type) {
        this.ticketID = UUID.randomUUID();
        this.event = event;
        this.owner = owner;
        this.type = type;
        ListOfAllTickets.put(this.ticketID, this);
    }
    public static void loadTicketData(HashMap<UUID, Ticket> list) {
        ListOfAllTickets = list;
    }
    public static HashMap<UUID, Ticket> getListOfAllTickets() {
        return ListOfAllTickets;
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

    public String toString(){
        return "Ticket ID " + this.ticketID.toString() + " class " + type + " for " + this.event.getInfo().name + " starting at " + this.event.getInfo().date;
    }

    // public void ChangeTicket(char newType) throws IllegalArgumentException{
    //     if (event.ticketTypes == null || !new String(event.ticketTypes).contains(String.valueOf(type))){
    //         throw new IllegalArgumentException("Invalid ticket type");
    //     }else{
    //         this.setType(newType);
    //     }
    // }
}
