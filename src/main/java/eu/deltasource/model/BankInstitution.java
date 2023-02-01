package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;

import java.util.*;

public class BankInstitution {

    private String bankName;
    private String bankAddress;
    private Map<String, Integer> numberOfCustomers;
    private Map<String, Double> priceList;

    public BankInstitution(String bankName, String bankAddress) {
        try {
            setBankName(bankName);
            setBankAddress(bankAddress);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
        this.numberOfCustomers = new HashMap<>();
        this.priceList = new HashMap<>();
        this.priceList.put("Tax to same bank", 0.02);
        this.priceList.put("Tax to different bank", 0.05);
        this.priceList.put("Exchange to same currency", 1.05);
        this.priceList.put("Exchange to different currency", 1.15);
    }

    public BankInstitution() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        if(bankName.isBlank()) {
            throw new InvalidInputException("Bank name cannot be blank.");
        }
        this.bankName = bankName;
    }

    public void setBankAddress(String bankAddress) {
        if(bankAddress.isBlank()) {
            throw new InvalidInputException("Bank address cannot be blank.");
        }
        this.bankAddress = bankAddress;
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
