package net.xiedada.eventinfo.exceptions;

public class BadStatusException extends IllegalStateException {
    public BadStatusException(String message) {
        super(message);
    }
    public BadStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
