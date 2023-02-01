package eu.deltasource.exception;

public class NotEnoughMoneyToTransferException extends RuntimeException{

    public NotEnoughMoneyToTransferException(String message) {
        super(message);
    }
}
