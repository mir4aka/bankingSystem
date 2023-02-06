package eu.deltasource.model;

import java.time.LocalDateTime;

/**
 * This is a model of transaction and keeps information about a transaction such as amount, time of the transaction, currency, etc....
 */
public class Transaction {
    private String sourceIban;
    private String targetIban;
    private String sourceCurrency;
    private String targetCurrency;
    private double amountTransferred;
    private double amountDeposited;
    private double amountWithdrawn;
    private double exchangeRate;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private LocalDateTime timestamp;

    public Transaction(String sourceIban, String targetIban, double amountTransferred, String sourceCurrency, String targetCurrency, double exchangeRate, LocalDateTime timestamp) {
        this.sourceIban = sourceIban;
        this.targetIban = targetIban;
        this.amountTransferred = amountTransferred;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public String getSourceIban() {
        return sourceIban;
    }

    public void setSourceIban(String sourceIban) {
        this.sourceIban = sourceIban;
    }

    public String getTargetIban() {
        return targetIban;
    }

    public void setTargetIban(String targetIban) {
        this.targetIban = targetIban;
    }

    public void setSourceBank(BankInstitution sourceBank) {
        this.sourceBank = sourceBank;
    }

    public void setTargetBank(BankInstitution targetBank) {
        this.targetBank = targetBank;
    }

    public double getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(double amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public double getAmountDeposited() {
        return amountDeposited;
    }

    public void setAmountDeposited(double amountDeposited) {
        this.amountDeposited = amountDeposited;
    }

    public double getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public void setAmountWithdrawn(double amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * This method returns boolean if the params below are null in case the transaction is a deposit or a withdraw, afterwards prints out the information.
     */
    private boolean checkIfEmptyValuesExistInTheTransaction() {
        return targetBank == null && targetCurrency == null && targetIban == null;
    }

    @Override
    public String toString() {
        if (checkIfEmptyValuesExistInTheTransaction()) {
            return String.format("""
                    Iban: %s
                    Bank name: %s
                    Transferred amount: %.2f
                    Deposited amount: %.2f
                    Withdrawn amount: %.2f
                    Currency: %s
                    Exchange rate: %.2f
                    Time of transaction: %s
                    """, sourceIban, sourceBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, exchangeRate, timestamp);
        }
        return String.format("""
                Source iban = %s
                Target iban: %s
                Source bank: %s
                Target bank: %s
                Transferred amount: %.2f
                Deposited amount: %.2f
                Withdrawn amount: %.2f
                Source currency: %s
                Target currency: %s
                Exchange rate: %.2f
                Time of transaction: %s
                """, sourceIban, targetIban, sourceBank, targetBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, targetCurrency, exchangeRate, timestamp);

    }
}
