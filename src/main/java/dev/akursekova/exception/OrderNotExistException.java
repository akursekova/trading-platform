package dev.akursekova.exception;

public class OrderNotExistException extends Exception {
    public OrderNotExistException() {
    }

    public OrderNotExistException(String message) {
        super(message);
    }

    public OrderNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
