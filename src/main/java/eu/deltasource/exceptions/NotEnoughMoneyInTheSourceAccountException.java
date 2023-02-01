package eu.deltasource.exceptions;

public class NotEnoughMoneyInTheSourceAccountException extends RuntimeException{
    public NotEnoughMoneyInTheSourceAccountException(String message) {
        super(message);
    }
}
