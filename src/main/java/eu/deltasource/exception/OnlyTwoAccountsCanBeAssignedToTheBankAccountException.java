package eu.deltasource.exception;

public class OnlyTwoAccountsCanBeAssignedToTheBankAccountException extends RuntimeException{
    public OnlyTwoAccountsCanBeAssignedToTheBankAccountException(String message) {
        super(message);
    }
}
