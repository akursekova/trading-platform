package dev.akursekova.exception;

public class TradeCreationException extends Exception {
    public TradeCreationException() {
    }

    public TradeCreationException(String message) {
        super(message);
    }

    public TradeCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
