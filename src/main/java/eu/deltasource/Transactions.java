package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class Transactions {
    private String sourceIban;
    private String targetIban;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private double amountTransferred;
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

    public double getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(double amountTransferred) {
        this.amountTransferred = amountTransferred;
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

    @Override
    public String toString() {
        return  String.format("sourceIban = %s\n" +
                "targetIban = %s\n" +
                "sourceBank = %s\n" +
                "targetBank = %s\n" +
                "Transferred amount: %.2f\n" +
                "sourceCurrency = %s\n" +
                "targetCurrency = %s\n" +
                "exchangeRate= %.2f\n" +
                "timeStamp = %s\n", sourceIban, targetIban, sourceBank,targetBank, amountTransferred, sourceCurrency, targetCurrency, exchangeRate, timestamp);
    }
}
