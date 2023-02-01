package eu.deltasource.exception;

public class NotAllowedToTransferToTheSameBankAccountException extends RuntimeException{

    public NotAllowedToTransferToTheSameBankAccountException(String message) {
        super(message);
    }
}
