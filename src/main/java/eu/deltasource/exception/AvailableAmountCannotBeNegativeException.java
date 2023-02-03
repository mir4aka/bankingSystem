package eu.deltasource.exception;

/**
 * If the customer tries to create a bank account with a negative balance exception.
 */
public class AvailableAmountCannotBeNegativeException extends RuntimeException{
    public AvailableAmountCannotBeNegativeException(String message) {
        super(message);
    }
}
