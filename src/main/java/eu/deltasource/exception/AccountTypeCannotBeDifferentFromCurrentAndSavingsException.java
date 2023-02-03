package eu.deltasource.exception;

/**
 * For invalid account type.
 */
public class AccountTypeCannotBeDifferentFromCurrentAndSavingsException extends RuntimeException{

    public AccountTypeCannotBeDifferentFromCurrentAndSavingsException(String message) {
        super(message);
    }
}
