package eu.deltasource.model;

import java.time.LocalDate;


public class Transactions {
    private String sourceIban;
    private String targetIban;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private double amountTransferred;
    private double amountDeposited;
    private double amountWithdrawn;
    private String sourceCurrency;
    private String targetCurrency;
    private double exchangeRate;
    private LocalDate timestamp;

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

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    private boolean checkIfEmptyValuesExistInTheTransaction() {
        return targetBank == null && targetCurrency == null && targetIban == null;
    }

    private String checkValuesOfTheTransactionObject() {
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

    @Override
    public String toString() {
        return checkValuesOfTheTransactionObject();
    }
}
