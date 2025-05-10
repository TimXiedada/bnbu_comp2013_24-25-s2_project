package net.xiedada.eventinfo.userutilities;

import net.xiedada.eventinfo.exceptions.*;

public abstract class User implements Authenticator {
    private final int userID;
    private String username;
    private String password; // stored in plain text for now, not safe at all QAQ
    private boolean isSignedIn;
    private static int userCount = 0; // static variable to keep track of the number of users
    boolean locked = false; // default value is false

    public static int getUserCount() {
        return userCount;
    }

    public User(int userID, String username, String password) { // only invoked when loading from user database
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.isSignedIn = false;
        userCount++;
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

}
