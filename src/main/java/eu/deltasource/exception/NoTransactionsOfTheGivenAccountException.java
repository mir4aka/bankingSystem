package eu.deltasource.exception;

public class NoTransactionsOfTheGivenAccountException extends RuntimeException{
    public NoTransactionsOfTheGivenAccountException(String message) {
        super(message);
    }

}
