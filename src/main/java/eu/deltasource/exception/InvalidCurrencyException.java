package eu.deltasource.exception;

/**
 * If the customer tries to create a bank account with an invalid currency exception.
 */
public class InvalidCurrencyException extends RuntimeException{

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
