package eu.deltasource.exception;

public class NotEnoughMoneyToWithdrawException extends RuntimeException{

    public NotEnoughMoneyToWithdrawException(String message) {
        super(message);
    }
}
