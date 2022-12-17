package dev.akursekova.exception;

public class OrderCreationException extends Exception{
    public OrderCreationException() {
    }

    public OrderCreationException(String message) {
        super(message);
    }

    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
