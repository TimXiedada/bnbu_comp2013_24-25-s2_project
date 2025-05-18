package net.xiedada.eventinfo.ui.icli;

import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*;
import net.xiedada.eventinfo.userutilities.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class iCLIMain {
    public static iCLIInputToolkit toolkit = new iCLIInputToolkit();

    // Main entry point for the interactive command line interface (iCLI)
    // application
    public static void WelcomePage(String[] args) {
        toolkit.PrintMSG("Welcome to the Event Information System iCLI user interface!");
        toolkit.PrintMSG("Copyright (c) 2023 Xie Youtian. All rights reserved.");
        String[] options = { "Login", "Register", "Exit" };
        int choice = 0;
        boolean exit = false;
        while (!exit) {
            choice = toolkit.Menu(options, "What would you like to do?");
            switch (choice) {
                case 1:
                    LoginForm();
                    break;
                case 2:
                    RegisterForm();
                    break;
                case 3:
                    toolkit.PrintMSG("Exiting...");
                    exit = true;
                    break;
                default:
                    toolkit.PrintMSG("Invalid choice. Please try again.");
                    break;
            }
        }

    }

    public static void LoginForm() {
        String Username, Password;
        User U = null;
        Username = toolkit.NonEmptyInput("Enter your username: ");
        Password = toolkit.PasswordInput("Enter your password: ");

        try {
            U = User.findUserWithUserName(Username);
            if (U == null || !U.login(Username, Password)) {
                throw new BadStatusException("Invalid username or password.");

            }
            toolkit.PrintMSG("Login successful. Welcome, " + U.getUsername() + "!");
            // Proceed to the main menu or user-specific actions
        } catch (Exception e) {
            toolkit.PrintMSG(e.getMessage());
            return;
        }
        switch (U.userType) {
            case ADMINISTRATOR:
                MainMenuAdmin((Administrator) U);
                break;
            case EVENT_ORGANIZER:
                MainMenuEventOrganizer((EventOrganizer) U);
                break;
            case CUSTOMER:
                MainMenuCustomer((Customer) U);
                break;
            default:
                toolkit.PrintMSG("Unknown user type.");
                break;
        }

    }

    public static void RegisterForm() {
        String Username, Password;
        User U = null; // leave unused as it will be used elsewhere
        int choice = toolkit.Menu(new String[] { "Customer", "Event Organizer", "Administrator" },
                "Which type of user would you like to register?");
        Username = toolkit.Input("Enter your username: ");
        while (true) {
            Password = toolkit.PasswordInput("Enter your password: ");
            if (Password.equals(toolkit.PasswordInput("Re-enter your password: "))) {
                break;
            } else {
                toolkit.PrintMSG("Passwords mismatch. Please try again.");
            }
        }
        try {
            switch (choice) {
                case 1:

                    U = new Customer(Username, Password);
                    toolkit.PrintMSG("Customer registered successfully.");
                    break;
                case 2:
                    U = new EventOrganizer(Username, Password);
                    toolkit.PrintMSG("Event Organizer registered successfully.");
                    break;
                case 3:
                    if (toolkit.PasswordInput("Input system admin password:").equals("iLoveBNBU")) {
                        // Although hardcoding password is ridiculous and risky,
                        // and this password can be easily compromised by using a decompiler,
                        // this is not a real system and no one will use it in real life
                        // --just a class project XwX
                        U = new Administrator(Username, Password);
                        toolkit.PrintMSG("Administrator registered successfully.");
                    } else {
                        toolkit.PrintMSG("Incorrect admin password. Registration failed.");
                    }
                    break;

                default:
                    toolkit.PrintMSG("Invalid choice.");
                    break;
            }
        } catch (IllegalArgumentException e) {
            toolkit.PrintMSG(e.getMessage());
        } catch (BadStatusException e) {
            toolkit.PrintMSG(e.getMessage());
        }

    }

    public static void MainMenuAdmin(Administrator admin) {
        toolkit.PrintMSG("Welcome, " + admin.getUsername() + "! You are logged in as an administrator.");
        String[] selections = new String[] { "List all users", "Suspend User", "Approve/Disapprove Event", "Exit" };
        while (true) {
            switch (toolkit.Menu(selections, "What would you like to do?")) {
                case 1:
                    toolkit.PrintMSG("List of all users:");
                    for (User user : User.getListofAllUsers()) {
                        toolkit.PrintMSG(user.toString());
                    }
                    continue;
                case 2:
                    toolkit.PrintMSG("List of all users:");
                    for (User user : User.getListofAllUsers()) {
                        toolkit.PrintMSG(user.toString());
                    }
                    String usernameOrID = toolkit
                            .NonEmptyInput("Enter the username or UID of the account you wanna suspend: ");
                    int uid2suspend;
                    try {
                        uid2suspend = Integer.parseInt(usernameOrID);
                        User userToSuspend = User.findUserWithUID(uid2suspend);
                        if (userToSuspend != null) {
                            admin.suspendUser(userToSuspend);
                            toolkit.PrintMSG("User " + userToSuspend.getUsername() + " has been suspended.");
                        } else {
                            toolkit.PrintMSG("User not found.");
                        }
                    } catch (NumberFormatException e) {
                        User userToSuspend = User.findUserWithUserName(usernameOrID);
                        if (userToSuspend != null) {
                            admin.suspendUser(userToSuspend);
                            toolkit.PrintMSG("User " + userToSuspend.getUsername() + " has been suspended.");
                        } else {
                            toolkit.PrintMSG("User not found.");
                        }
                    }
                    continue;
                case 3:
                    ArrayList<Event> eventList2Approve = new ArrayList<Event>();
                    for (Event event2approve : Event.getListOfAllEvents()) {
                        if (event2approve.getStatus() == Event.EventStatus.APPLYING) {
                            eventList2Approve.add(event2approve);
                        }
                    }
                    int eventOpIdx = toolkit.Menu(
                            eventList2Approve.stream().map(Event::toString).toArray(String[]::new),
                            "Select a event from this list to approve:") - 1;
                    if (eventOpIdx < 0 || eventOpIdx >= eventList2Approve.size()) {
                        toolkit.PrintMSG("Invalid selection.");
                        continue;
                    }
                    toolkit.PrintMSG("Selected event: " + eventList2Approve.get(eventOpIdx).toString());
                    int operation = toolkit.Menu(new String[] { "Approve", "Disapprove", "Back" },
                            "What do you want to do?");
                    if (operation == 1) {
                        try {
                            admin.approveEvent(eventList2Approve.get(eventOpIdx));
                            toolkit.PrintMSG("Event approved successfully.");
                        } catch (BadStatusException e) {
                            toolkit.PrintMSG(e.getMessage());
                        }
                    } else if (operation == 2) {
                        admin.disapproveEvent(eventList2Approve.get(eventOpIdx));
                        toolkit.PrintMSG("Event disapproved successfully.");
                    } else if (operation == 3) {
                        toolkit.PrintMSG("Back to main menu.");
                        continue;
                    } else {
                        toolkit.PrintMSG("Invalid operation.");
                    }
                    continue;
                case 4:
                    toolkit.PrintMSG("Exiting...");
                    admin.logout();
                    return;
                default:
                    break;
            }
        }

    }

    // public static void MainMenuCustomer(Customer customer) {
    //     toolkit.PrintMSG("Welcome, " + customer.getUsername() + "! You are logged in as a customer.");
    //     String[] selections = new String[] { "View Available Events", "Book Ticket", "View My Tickets", "Return/Change Ticket", "Exit" };
    //     while (true) {
    //         switch (toolkit.Menu(selections, "What would you like to do?")) {
    //             case 1:
    //                 toolkit.PrintMSG("Available events:");
    //                 for (Event event : Event.getListOfAllEvents()) {
    //                     if (event.getStatus() == Event.EventStatus.AVAILABLE || event.getStatus() == Event.EventStatus.APPROVED) {
    //                         toolkit.PrintMSG(event.toString());
    //                     }
    //                 }
    //             case 2:               
    //             try {
    //                 Event[] availableEvents = Event.getListOfAllEvents().stream().filter(event -> event.getStatus() == Event.EventStatus.AVAILABLE).toArray(Event[]::new);
    //                 int selectedIdx = toolkit.menu(Arrays.stream(availableEvents).map(Event::toString).toArray(String[]::new),"Select an event to book:");           
    //                 Event selectedEvent = availableEvents[selectedIdx];
    //                 char ticketType = toolkit.NonEmptyInput("Enter the ticket type: ").charAt(0);
    //                 Ticket ticket = customer.bookTicket(selectedEvent, ticketType);
    //                 if (ticket == null) {
    //                     toolkit.PrintMSG("Failed to book ticket. Event may be overdued or capacity may be full.");
    //                     continue;
    //                 }

    //                 toolkit.PrintMSG("Event booked successfully: " + selectedEvent);
    //             } catch (ArrayIndexOutOfBoundsException e) {
    //                 toolkit.PrintMSG("Invalid selection. Please try again.");
    //             }
                
    //             continue;

    //             case 3:
    //                 toolkit.PrintMSG("Your tickets:");
    //                 for (Ticket ticket : customer.getTickets()) {
    //                     toolkit.PrintMSG(ticket.toString());
    //                 }
    //                 continue;
    //             case 4:
    //                 String ticketID = toolkit.NonEmptyInput("Enter the Ticket ID to return: ");
    //                 try {
    //                     Ticket ticketToReturn = Ticket.getListOfAllTickets().get(UUID.fromString(ticketID));
    //                     if (ticketToReturn != null && ticketToReturn.getOwner().equals(customer)) {
    //                         customer.ReturnTicket(ticketToReturn);
    //                         toolkit.PrintMSG("Ticket returned successfully: " + ticketToReturn);
    //                     } else {
    //                         toolkit.PrintMSG("Ticket not found or does not belong to you.");
    //                     }
    //                 } catch (Exception e) {
    //                     toolkit.PrintMSG("Error: " + e.getMessage());
    //                 }
    //                 continue;
    //             case 5:
    //                 String ticketIDToChange = toolkit.NonEmptyInput("Enter the Ticket ID to change: ");
    //                 try {
    //                     Ticket ticketToChange = Ticket.getListOfAllTickets().get(UUID.fromString(ticketIDToChange));
    //                     if (ticketToChange != null && ticketToChange.getOwner().equals(customer)) {
    //                         toolkit.PrintMSG("Current ticket details: " + ticketToChange);
    //                         char newTicketType = toolkit.NonEmptyInput("Enter the new ticket type: ").charAt(0);
    //                             customer.ChangeTicket(ticketToChange, newTicketType);
    //                             toolkit.PrintMSG("Ticket changed successfully to event: " + newEvent);
    //                         } else {
    //                             toolkit.PrintMSG("New event not found.");
    //                         }
    //                     } else {
    //                         toolkit.PrintMSG("Ticket not found or does not belong to you.");
    //                     }
    //                 } catch (Exception e) {
    //                     toolkit.PrintMSG("Error: " + e.getMessage());
    //                 }
    //                 continue;
    //             case 6:
    //                 toolkit.PrintMSG("Exiting...");
    //                 customer.logout();
    //                 return;
    //             default:
    //                 toolkit.PrintMSG("Invalid choice. Please try again.");
    //                 break;
    //         }
    //     }

    // }

    public static void MainMenuEventOrganizer(EventOrganizer eventOrganizer) {
        toolkit.PrintMSG("Welcome, " + eventOrganizer.getUsername() + "! You are logged in as an event organizer.");
        String[] selections = new String[] { "Create Event", "View My Events", "Manage an Event", "Exit" };
        String eventName, description, location, dateInput;
        int capacity;
        char[] ticketTypes;
        while (true) {
            switch (toolkit.Menu(selections, "What would you like to do?")) {
                case 1:

                    try {
                        eventName = toolkit.NonEmptyInput("Enter the name of the event: ");
                        description = toolkit.NonEmptyInput("Enter the description of the event: ");
                        location = toolkit.NonEmptyInput("Enter the location of the event: ");
                        dateInput = toolkit.NonEmptyInput("Enter the date of the event (YYYY-MM-DD): ");
                        capacity = Integer.parseInt(toolkit.NonEmptyInput("Enter the capacity of the event: "));
                        ticketTypes = toolkit.NonEmptyInput("Enter ticket types (e.g., A,B,C): ").replaceAll(" ", "")
                                .toUpperCase().toCharArray();
                        LocalDate date = LocalDate.parse(dateInput);
                        eventOrganizer.createEvent(eventName, description, location, date, capacity, ticketTypes);
                        toolkit.PrintMSG("Event created successfully.");
                    } catch (Exception e) {
                        toolkit.PrintMSG("Error: " + e.getMessage());
                    }
                    continue;
                case 2:
                    toolkit.PrintMSG("Your events:");
                    for (Event event : eventOrganizer.getEvents()) {
                        toolkit.PrintMSG(event.toString());
                    }
                    continue;
                case 3:
                    ArrayList<Event> eventList = eventOrganizer.getEvents();
                    ArrayList<String> eventNames = new ArrayList<>();
                    for (Event e : eventList) {
                        eventNames.add(e.toString());
                    }
                    int selectedEventIdx = toolkit.Menu(eventNames.toArray(new String[0]),
                            "Select an event to operate:") - 1;
                    int operation = toolkit.Menu(new String[] { "Update", "Cancel", "Start ordering/applying", "Back" },
                            "Which operation would you like to perform?");

                    switch (operation) {
                        case 1:
                            try {
                                eventName = toolkit.NonEmptyInput("Enter the name of the event: ");
                                description = toolkit.NonEmptyInput("Enter the description of the event: ");
                                location = toolkit.NonEmptyInput("Enter the location of the event: ");
                                dateInput = toolkit.NonEmptyInput("Enter the date of the event (YYYY-MM-DD): ");
                                capacity = Integer.parseInt(toolkit.NonEmptyInput("Enter the capacity of the event: "));

                                Event eventToUpdate = eventOrganizer.getEvents().get(selectedEventIdx);
                                Event.EventData newData = eventToUpdate.new EventData(eventName, description, location,
                                        LocalDate.parse(dateInput), capacity);

                                eventToUpdate.updateEventData(newData);
                                toolkit.PrintMSG("Event updated successfully.");
                            } catch (Exception e) {
                                toolkit.PrintMSG("Error: " + e.getMessage());
                            }
                            continue;

                        case 2:
                            Event eventToCancel = eventOrganizer.getEvents().get(selectedEventIdx);
                            try {
                                eventToCancel.cancel();
                                toolkit.PrintMSG("Event cancelled successfully.");
                            } catch (Exception e) {
                                toolkit.PrintMSG("Error: " + e.getMessage());
                            }
                            toolkit.PrintMSG("Event cancelled successfully.");
                            continue;
                        case 3:
                            Event eventToStart = eventOrganizer.getEvents().get(selectedEventIdx);
                            if (eventToStart.getStatus() == Event.EventStatus.APPROVED) {
                                try {
                                    eventToStart.setAvailable();
                                    toolkit.PrintMSG("Event is now available for booking.");
                                } catch (Exception e) {
                                    toolkit.PrintMSG("Error: " + e.getMessage());
                                }
                            } else if (eventToStart.getStatus() == Event.EventStatus.AVAILABLE) {
                                toolkit.PrintMSG("Event is already available for booking.");
                            } else if (eventToStart.getStatus() == Event.EventStatus.PLANNING) {
                                eventToStart.setApplying();
                                toolkit.PrintMSG("Event is now in the application phase.");
                            }
                            continue;
                        case 4:
                            toolkit.PrintMSG("Back to main menu.");
                            continue;
                        default:
                            toolkit.PrintMSG("Invalid operation. Please try again.");
                            break;
                    }
                    continue;
                case 4:
                    toolkit.PrintMSG("Exiting...");
                    eventOrganizer.logout();
                    return;
                default:
                    toolkit.PrintMSG("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
