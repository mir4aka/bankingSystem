package eu.deltasource.exceptions;

public class NotEnoughMoneyToTransferException extends RuntimeException{
    public NotEnoughMoneyToTransferException(String message) {
        super(message);
    }
}
