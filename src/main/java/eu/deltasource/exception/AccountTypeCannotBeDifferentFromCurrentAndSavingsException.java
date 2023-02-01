package eu.deltasource.exception;

public class AccountTypeCannotBeDifferentFromCurrentAndSavingsException extends RuntimeException{

    public AccountTypeCannotBeDifferentFromCurrentAndSavingsException(String message) {
        super(message);
    }
}
