package eu.deltasource;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Transactions {
    private String sourceIban;
    private String targetIban;
    private BankInstitution sourceBank;
    private BankInstitution targetBank;
    private List<String> amountTransferred = new ArrayList<>();
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

    public List<String> getAmountTransferred() {
        return amountTransferred;
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        amountTransferred.forEach(t -> {
            sb.append(t).append(System.lineSeparator());
        });

        return  String.format("sourceIban = %s\n" +
                "targetIban = %s\n" +
                "sourceBank = %s\n" +
                "targetBank = %s\n" +
                "%s" +
                "sourceCurrency = %s\n" +
                "targetCurrency = %s\n" +
                "exchangeRate= %.2f\n" +
                "timeStamp = %s\n", sourceIban, targetIban, sourceBank,targetBank, sb.toString(), sourceCurrency, targetCurrency, exchangeRate, timeStamp);
    }
}
