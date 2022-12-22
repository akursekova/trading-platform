package dev.akursekova.exception;

public class SecurityNotExistException extends Exception {
    public SecurityNotExistException() {
    }

    public SecurityNotExistException(String message) {
        super(message);
    }

    public SecurityNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
