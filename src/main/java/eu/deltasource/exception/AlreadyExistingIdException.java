package eu.deltasource.exception;

public class AlreadyExistingIdException extends RuntimeException{

    public AlreadyExistingIdException(String errorMessage) {
        super(errorMessage);
    }
}
