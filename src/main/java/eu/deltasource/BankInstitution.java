package eu.deltasource;

import eu.deltasource.exceptions.*;

import java.time.LocalDate;
import java.util.*;

public class BankInstitution {
    private String bankName;
    private String bankAddress;
    private Map<String, Integer> numberOfCustomers;
    private Map<String, Double> priceList;

    public BankInstitution(String bankName, String bankAddress) {
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.numberOfCustomers = new HashMap<>();
        this.priceList = new HashMap<>();
        this.priceList.put("Tax to same bank", 0.02);
        this.priceList.put("Tax to different bank", 0.05);
        this.priceList.put("Exchange to same currency", 1.05);
        this.priceList.put("Exchange to different currency", 1.15);
    }

    public String getBankName() {
        return bankName;
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    public Map<String, Integer> getNumberOfCustomers() {
        return this.numberOfCustomers;
    }

    @Override
    public String toString() {
        return bankName + " bank" + System.lineSeparator() +
                "The address of the bank is: " + bankAddress + System.lineSeparator() +
                "Number of customers of the bank: " + numberOfCustomers.size();
    }
}
