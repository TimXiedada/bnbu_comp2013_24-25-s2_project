package net.xiedada.eventinfo.userutilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import net.xiedada.eventinfo.exceptions.*;

public abstract class User implements Authenticator, Serializable {
    public static enum UserType {
        ADMINISTRATOR,
        EVENT_ORGANIZER,
        CUSTOMER
    }

    private static ArrayList<User> listofAllUsers = new ArrayList<User>(); // static variable to keep track of all users
    private static HashMap<Integer, User> userIDMap = new HashMap<>(); // static variable to keep track of user IDs
    private static HashMap<String, User> userNameMap = new HashMap<>(); // static variable to keep track of usernames

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

    public static ArrayList<User> getListofAllUsers() {
        return listofAllUsers;
    }
    public static void loadUserList(ArrayList<User> list) {
        listofAllUsers = list;
        // rebuild the userIDMap and userNameMap
        userIDMap.clear(); // clear existing mappings (if any)
        userNameMap.clear(); // clear existing mappings (if any)
        for (User user : listofAllUsers) {
            userIDMap.put(user.userID, user); // add user to userIDMap
            userNameMap.put(user.username, user); // add user to userNameMap
        }
    }
    public User(String username, String password, UserType userType)
            throws IllegalArgumentException, BadStatusException { // only invoked when creating a new user
        if (java.util.regex.Pattern.matches("^[a-z_]([a-z0-9_-]{0,31}|[a-z0-9_-]{0,30}\\$)$", username))
            this.username = username;
        else
            throw new IllegalArgumentException("Invalid username format");
        if (User.findUserWithUserName(username) != null) {
            throw new BadStatusException("User already exists");
        }
        this.password = password;
        this.isSignedIn = false;
        this.userType = userType;
        listofAllUsers.add(this);
        this.userID = listofAllUsers.size() + 1; // set userID to the size of the list of all users
        userIDMap.put(this.userID, this); // add user to userIDMap
        userNameMap.put(this.username, this); // add user to userNameMap
    }

    public User(int userID, String username, String password, UserType userType) throws IllegalArgumentException { // only
                                                                                                                   // invoked
                                                                                                                   // when
                                                                                                                   // loading
                                                                                                                   // from
                                                                                                                   // user
                                                                                                                   // database
        this.userID = userIDCounter++;
        if (java.util.regex.Pattern.matches("^[a-z_]([a-z0-9_-]{0,31}|[a-z0-9_-]{0,30}\\$)$", username))
            this.username = username;
        else
            throw new IllegalArgumentException("Invalid username format");
        this.password = password;
        this.isSignedIn = false;
        this.userType = userType;
        listofAllUsers.add(this);
        userIDMap.put(this.userID, this); // add user to userIDMap
        userNameMap.put(this.username, this); // add user to userNameMap
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

    public static User findUserWithUserName(String username) {
        if (userNameMap.containsKey(username)) {
            return userNameMap.get(username);
        } else
            return null;
    }

    public static User findUserWithUID(int UID) {
        if (userIDMap.containsKey(UID)) {
            return userIDMap.get(UID);
        } else
            return null;
    }

    public String toString() {
        HashMap<UserType, String> userTypeMap = new HashMap<>();
        userTypeMap.put(UserType.ADMINISTRATOR, "Administrator");
        userTypeMap.put(UserType.EVENT_ORGANIZER, "Event organizer");
        userTypeMap.put(UserType.CUSTOMER, "Customer");
        return userTypeMap.get(userType) + " " + username + " (UID: " + userID + (locked ? ", suspended" : "") + ")";
    }
}
