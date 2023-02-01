package eu.deltasource.exceptions;

public class UnableToDepositMoneyFromADifferentBankToTheOriginalOneException extends RuntimeException{
    public UnableToDepositMoneyFromADifferentBankToTheOriginalOneException(String message) {
        super(message);
    }
}
