package eu.deltasource.exception;

/**
 * If a customer tries to transfer money without having the sufficient amount to do so.
 */
public class NotEnoughMoneyToTransferException extends RuntimeException{

    public NotEnoughMoneyToTransferException(String message) {
        super(message);
    }
}
