package net.xiedada.eventinfo.exceptions;

public class SuspendException extends Exception {
    public SuspendException(String message) {
        super(message);
    }

    public SuspendException(String message, Throwable cause) {
        super(message, cause);
    }

}
