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


    public void setSourceIban(String sourceIban) {
        this.sourceIban = sourceIban;
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

    public void setAmountTransferred(double amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public void setAmountDeposited(double amountDeposited) {
        this.amountDeposited = amountDeposited;
    }

    public void setAmountWithdrawn(double amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
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
                    Source Iban: %s
                    Bank: %s
                    Transferred amount: %.2f
                    Deposited amount: %.2f
                    Withdrawn amount: %.2f
                    Currency: %s
                    ExchangeRate: %.2f
                    timestamp: %s
                    """, sourceIban, sourceBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, exchangeRate, timestamp);
        }
        return String.format("""
                Source Iban = %s
                Target Iban: %s
                Source Bank: %s
                Target Bank: %s
                Transferred amount: %.2f
                Deposited amount: %.2f
                Withdrawn amount: %.2f
                Source Currency: %s
                Target Currency: %s
                ExchangeRate: %.2f
                timestamp: %s
                >------------------------------<
                """, sourceIban, targetIban, sourceBank, targetBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, targetCurrency, exchangeRate, timestamp);
    }

    @Override
    public String toString() {
        return checkValuesOfTheTransactionObject();
    }
}
