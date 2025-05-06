package net.xiedada.eventinfo.userutilities;

public abstract class User implements CanLogin{
    private final int userID;
    private String username;
    private String password; // stored in plain text for now, not safe at all QAQ
    private boolean isSignedIn;

    public User(int userID, String username, String password){ // only invoked when loading from user database 
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.isSignedIn = false;
    }

    public boolean login(String username, String password) throws IllegalStateException{
        if (isSignedIn){
            throw new IllegalStateException("User is already logged in.");
        }
        if (username.equals(this.username) && password.equals(this.password)){
            isSignedIn = true;
            return true;
        } else return false;
    }

    public boolean logout() throws IllegalStateException {
        isSignedIn = false;
        return true;
    }
    public abstract void suspendUser();

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
    
    
    


