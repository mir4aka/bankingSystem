package eu.deltasource.exception;

public class InvalidCurrencyException extends RuntimeException{

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
