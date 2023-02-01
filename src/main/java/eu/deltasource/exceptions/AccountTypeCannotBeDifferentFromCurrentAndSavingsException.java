package eu.deltasource.exceptions;

public class AccountTypeCannotBeDifferentFromCurrentAndSavingsException extends RuntimeException{
    public AccountTypeCannotBeDifferentFromCurrentAndSavingsException(String message) {
        super(message);
    }
}
