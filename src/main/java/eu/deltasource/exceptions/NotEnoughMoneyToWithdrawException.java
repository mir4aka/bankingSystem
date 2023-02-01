package eu.deltasource.exceptions;

public class NotEnoughMoneyToWithdrawException extends RuntimeException{
    public NotEnoughMoneyToWithdrawException(String message) {
        super(message);
    }
}
