package eu.deltasource.exception;

/**
 * For already existing account with the given id exception.
 */
public class AlreadyExistingIdException extends RuntimeException{

    public AlreadyExistingIdException(String errorMessage) {
        super(errorMessage);
    }
}
