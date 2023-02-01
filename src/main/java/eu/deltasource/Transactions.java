package eu.deltasource;

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

    public BankInstitution getTargetBank() {
        return targetBank;
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

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
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

    @Override
    public String toString() {
        if(checkIfEmptyValuesExistInTheTransaction()) {
           return String.format("sourceIban = %s\n" +
                    "sourceBank = %s\n" +
                    "Transferred amount: %.2f\n" +
                    "Deposited amount: %.2f\n" +
                    "Withdrawn amount: %.2f\n" +
                    "sourceCurrency = %s\n" +
                    "exchangeRate= %.2f\n" +
                    "timeStamp = %s\n", sourceIban, sourceBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, exchangeRate, timestamp);
        }
        return String.format("sourceIban = %s\n" +
                "targetIban = %s\n" +
                "sourceBank = %s\n" +
                "targetBank = %s\n" +
                "Transferred amount: %.2f\n" +
                "Deposited amount: %.2f\n" +
                "Withdrawn amount: %.2f\n" +
                "sourceCurrency = %s\n" +
                "targetCurrency = %s\n" +
                "exchangeRate= %.2f\n" +
                "timeStamp = %s\n", sourceIban, targetIban, sourceBank, targetBank, amountTransferred, amountDeposited, amountWithdrawn, sourceCurrency, targetCurrency, exchangeRate, timestamp);
    }
}
