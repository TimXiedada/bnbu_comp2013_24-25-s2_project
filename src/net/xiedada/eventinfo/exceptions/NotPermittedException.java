package net.xiedada.eventinfo.exceptions;

public class NotPermittedException extends IllegalAccessException {
    public NotPermittedException(String message) {
        super(message);
    }
}
