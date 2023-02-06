package eu.deltasource.exception;

/**
 * If a customer tries to withdraw money, but does not have enough to withdraw exception.
 */
public class NotEnoughMoneyToWithdrawException extends RuntimeException{

    public NotEnoughMoneyToWithdrawException(String message) {
        super(message);
    }
}
