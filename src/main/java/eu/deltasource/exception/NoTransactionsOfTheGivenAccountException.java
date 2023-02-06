package eu.deltasource.exception;

/**
 * If the customer tries to see his transactions, but there are not any exception.
 */
public class NoTransactionsOfTheGivenAccountException extends RuntimeException{
    public NoTransactionsOfTheGivenAccountException(String message) {
        super(message);
    }

}
