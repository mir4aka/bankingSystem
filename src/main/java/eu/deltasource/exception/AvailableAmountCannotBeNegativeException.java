package eu.deltasource.exception;

public class AvailableAmountCannotBeNegativeException extends RuntimeException{
    public AvailableAmountCannotBeNegativeException(String message) {
        super(message);
    }
}
