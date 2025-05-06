package net.xiedada.eventinfo.userutilities;

public interface CanLogin {
    public boolean login(String username, String password);
    public boolean logout();
}
