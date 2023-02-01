package eu.deltasource.exception;

public class TransfersAllowedBetweenCurrentAccountsException extends RuntimeException{

    public TransfersAllowedBetweenCurrentAccountsException(String message) {
        super(message);
    }
}
