package net.xiedada.eventinfo.userutilities;
import net.xiedada.eventinfo.exceptions.*;

// Interface for user authentication，formerly named CanLogin, an ugly name
public interface Authenticator { 
    public boolean login(String username, String password) throws SuspendException, BadStatusException;
    public boolean logout();
}
