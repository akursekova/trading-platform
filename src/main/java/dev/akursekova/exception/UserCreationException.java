package dev.akursekova.exception;

public class UserCreationException extends Exception {
    public UserCreationException() {
    }

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
