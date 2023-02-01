package eu.deltasource.exceptions;

public class TransfersAllowedBetweenCurrentAccountsException extends RuntimeException{
    public TransfersAllowedBetweenCurrentAccountsException(String message) {
        super(message);
    }
}
