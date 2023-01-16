package eu.deltasource;

public class AlreadyExistingIdException extends RuntimeException{
    public AlreadyExistingIdException(String errorMessage) {
        super(errorMessage);
    }
}
