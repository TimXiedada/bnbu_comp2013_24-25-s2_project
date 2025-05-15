package net.xiedada.eventinfo.userutilities;
import java.io.Serializable;
import java.util.ArrayList;
import net.xiedada.eventinfo.exceptions.*;

public abstract class User implements Authenticator, Serializable {
    public static enum UserType {
        ADMINISTRATOR,
        EVENT_ORGANIZER,
        CUSTOMER
    }
    private static ArrayList<User> listofAllUsers = new ArrayList<User>(); // static variable to keep track of all users
    static int userIDCounter = 0; // static variable to keep track of user IDs
    public final int userID;
    private String username;
    private String password; // stored in plain text for now, not safe at all QAQ
    private boolean isSignedIn;
    boolean locked = false; // default value is false
    public final UserType userType;
    public static int getUserCount() {
        return listofAllUsers.size();
    }

    

    public User(int userID, String username, String password, UserType userType) { // only invoked when loading from user database
        this.userID = userIDCounter++;
        this.username = username;
        this.password = password;
        this.isSignedIn = false;
        this.userType = userType;
    }

    public boolean login(String username, String password) throws SuspendException, BadStatusException {
        if (isSignedIn) {
            throw new IllegalStateException("User is already logged in.");
        }
        if (this.locked) {
            throw new BadStatusException("User is suspended and cannot log in.");
        }
        if (username.equals(this.username) && password.equals(this.password)) {
            isSignedIn = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean logout() throws IllegalStateException {
        isSignedIn = false;
        return true;
    }

    public abstract void suspendUser(User user) throws IllegalArgumentException, IllegalStateException;

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public static User findUserWithUID(int UID){
        for (User user : listofAllUsers) {
            if (user.getUserID() == UID) {
                return user;
            }
        }
        return null;
    }

}
