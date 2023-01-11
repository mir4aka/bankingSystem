package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayDeque;

public class Transactions {
    private String sourceIban;
    private String targetIban;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private ArrayDeque<String> amountTransferred = new ArrayDeque<>();
    private String sourceCurrency;
    private String targetCurrency;
    private double exchangeRate;
    private Timestamp timeStamp;


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

    public BankInstitution getSourceBank() {
        return sourceBank;
    }

    public void setSourceBank(BankInstitution sourceBank) {
        this.sourceBank = sourceBank;
    }

    public BankInstitution getTargetBank() {
        return targetBank;
    }

    public void setTargetBank(BankInstitution targetBank) {
        this.targetBank = targetBank;
    }

    public ArrayDeque<String> getAmountTransferred() {
        return this.amountTransferred;
    }

    public void setAmountTransferred(ArrayDeque<String> amountTransferred) {
        this.amountTransferred = amountTransferred;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
