package eu.deltasource.exception;

/**
 * If a customer tries to transfer money from and to a different account than `Current`.
 */
public class TransfersAllowedBetweenCurrentAccountsException extends RuntimeException{

    public TransfersAllowedBetweenCurrentAccountsException(String message) {
        super(message);
    }
}
