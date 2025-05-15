package net.xiedada.eventinfo.ui.icli;

import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*;
import net.xiedada.eventinfo.userutilities.*;

public class iCLIMain {
    public static iCLIInputToolkit toolkit = new iCLIInputToolkit();

    // Main entry point for the interactive command line interface (iCLI)
    // application
    public static void WelcomePage(String[] args) {
        System.out.println("Welcome to the Event Information System iCLI user interface!");
        System.out.println("Copyright (c) 2023 Xie Youtian. All rights reserved.");
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
                    System.out.println("Exiting...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
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
            System.out.println("Login successful. Welcome, " + U.getUsername() + "!");
            // Proceed to the main menu or user-specific actions
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                System.out.println("Unknown user type.");
                break;
        }

    }

    public static void RegisterForm() {
        String Username, Password;
        User U = null; //leave unused as it will be used elsewhere
        int choice = toolkit.Menu(new String[] { "Customer", "Event Organizer", "Administrator" },
                "Which type of user would you like to register?");
        Username = toolkit.Input("Enter your username: ");
        while (true) {
            Password = toolkit.PasswordInput("Enter your password: ");
            if (Password.equals(toolkit.PasswordInput("Re-enter your password: "))) {
                break;
            } else {
                System.out.println("Passwords mismatch. Please try again.");
            }
        }
        try {
            switch (choice) {
                case 1:

                    U = new Customer(Username, Password);
                    System.out.println("Customer registered successfully.");
                    break;
                case 2:
                    U = new EventOrganizer(Username, Password);
                    System.out.println("Event Organizer registered successfully.");
                    break;
                case 3:
                    if (toolkit.PasswordInput("Input system admin password:").equals("iLoveBNBU")) {
                        // Although hardcoding password is ridiculous and risky,
                        // and this password can be easily compromised by using a decompiler,
                        // this is not a real system and no one will use it in real life
                        // --just a class project XwX
                        U = new Administrator(Username, Password);
                        System.out.println("Administrator registered successfully.");
                    } else {
                        System.out.println("Incorrect admin password. Registration failed.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (BadStatusException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void MainMenuAdmin(Administrator admin) {
        System.out.println("Welcome, " + admin.getUsername() + "! You are logged in as an administrator.");
        String[] selections = new String[] { "List all users", "Suspend User", "Approve/Disapprove Event", "Exit" };
        while (true) {
            switch (toolkit.Menu(selections, "What would you like to do?")) {
                case 1:
                    System.out.println("List of all users:");
                    for (User user : User.getListofAllUsers()) {
                        System.out.println(user);
                    }
                    continue;
                case 2:
                    System.out.println("List of all users:");
                    for (User user : User.getListofAllUsers()) {
                        System.out.println(user);
                    }
                    String usernameOrID = toolkit
                            .NonEmptyInput("Enter the username or UID of the account you wanna suspend: ");
                    int uid2suspend;
                    try {
                        uid2suspend = Integer.parseInt(usernameOrID);
                        User userToSuspend = User.findUserWithUID(uid2suspend);
                        if (userToSuspend != null) {
                            admin.suspendUser(userToSuspend);
                            System.out.println("User " + userToSuspend.getUsername() + " has been suspended.");
                        } else {
                            System.out.println("User not found.");
                        }
                    } catch (NumberFormatException e) {
                        User userToSuspend = User.findUserWithUserName(usernameOrID);
                        if (userToSuspend != null) {
                            admin.suspendUser(userToSuspend);
                            System.out.println("User " + userToSuspend.getUsername() + " has been suspended.");
                        } else {
                            System.out.println("User not found.");
                        }
                    }
                    continue;
                case 3:
                    System.out.println("Coming soon...");
                    continue;
                default:
                    break;
            }
        }

    }

    public static void MainMenuCustomer(Customer customer) {
        System.out.println("Welcome, " + customer.getUsername() + "! You are logged in as a customer.");
        toolkit.Input("Press enter to log out.");
        customer.logout();
        return;
    }

    public static void MainMenuEventOrganizer(EventOrganizer eventOrganizer) {
        System.out.println("Welcome, " + eventOrganizer.getUsername() + "! You are logged in as an event organizer.");
        toolkit.Input("Press enter to log out.");
        eventOrganizer.logout();
        return;
    }
}
