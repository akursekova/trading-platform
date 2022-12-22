package dev.akursekova.exception;

public class TradeNotExistException extends Exception {
    public TradeNotExistException() {
    }

    public TradeNotExistException(String message) {
        super(message);
    }

    public TradeNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
