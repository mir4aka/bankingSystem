package eu.deltasource.exceptions;

public class SourceAccountsBankIsNotValidException extends RuntimeException{
    public SourceAccountsBankIsNotValidException(String message) {
        super(message);
    }
}
