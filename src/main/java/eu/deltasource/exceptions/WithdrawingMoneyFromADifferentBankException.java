package eu.deltasource.exceptions;

public class WithdrawingMoneyFromADifferentBankException extends RuntimeException{
    public WithdrawingMoneyFromADifferentBankException(String message) {
        super(message);
    }
}
