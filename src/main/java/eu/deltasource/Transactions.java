package eu.deltasource;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class Transactions {
    private String sourceIban;
    private String targetIban;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private Map<String, Timestamp> amountTransferred = new LinkedHashMap<>();
    private String sourceCurrency;
    private String targetCurrency;
    private double exchangeRate;
    private Timestamp timeStamp;

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

    public Map<String, Timestamp> getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(Map<String, Timestamp> amountTransferred) {
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

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
