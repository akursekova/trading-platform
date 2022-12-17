package dev.akursekova.exception;

public class SecurityCreationException extends Exception{
    public SecurityCreationException() {
    }

    public SecurityCreationException(String message) {
        super(message);
    }

    public SecurityCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
