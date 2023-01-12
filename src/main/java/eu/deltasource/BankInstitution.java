package eu.deltasource;

import java.util.*;

public class BankInstitution {
    private String bankName;
    private String bankAddress;
    private BankAccount account;
    private int numberOfCustomers;
    private Map<String, Double> priceList;

    public BankInstitution(String bankName, String bankAddress, int numberOfCustomers) {
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.numberOfCustomers = numberOfCustomers;
        this.priceList = new HashMap<>();
        this.priceList.put("Tax to same bank", 0.02);
        this.priceList.put("Tax to different bank", 0.05);
        this.priceList.put("Exchange to same bank", 1.05);
        this.priceList.put("Exchange to different bank", 1.15);
    }

    public String getBankName() {
        return bankName;
    }
    public BankAccount getAccount() {
        return account;
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    @Override
    public String toString() {
        return "The name of the bank is: " + this.bankName + System.lineSeparator() +
                "The address of the bank is: " + this.bankAddress + System.lineSeparator() +
                "Number of customers of the bank: " + this.numberOfCustomers + System.lineSeparator();
    }

}
