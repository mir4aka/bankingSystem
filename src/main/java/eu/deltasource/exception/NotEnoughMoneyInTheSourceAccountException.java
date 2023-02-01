package eu.deltasource.exception;

public class NotEnoughMoneyInTheSourceAccountException extends RuntimeException{

    public NotEnoughMoneyInTheSourceAccountException(String message) {
        super(message);
    }
}
