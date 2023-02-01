package eu.deltasource.exceptions;

public class NotAllowedToTransferToTheSameBankAccountException extends RuntimeException{
    public NotAllowedToTransferToTheSameBankAccountException(String message) {
        super(message);
    }
}
