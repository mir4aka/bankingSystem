package eu.deltasource.exception;

/**
 * If a customer tries to transfer money to his own account exception.
 */
public class NotAllowedToTransferToTheSameBankAccountException extends RuntimeException{

    public NotAllowedToTransferToTheSameBankAccountException(String message) {
        super(message);
    }
}
