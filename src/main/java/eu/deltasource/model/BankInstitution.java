package eu.deltasource.model;

import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.exception.InvalidInputException;

import java.util.*;

public class BankInstitution {

    private String bankName;
    private String bankAddress;
    private Map<String, Integer> numberOfCustomers;
    private Map<String, Double> priceList;

    private List<Transactions> transactions;

    public BankInstitution(String bankName, String bankAddress) {
        this.transactions = new ArrayList<>();
        setBankName(bankName);
        setBankAddress(bankAddress);
        this.numberOfCustomers = new HashMap<>();
        this.priceList = new HashMap<>();
        this.priceList.put("Tax to same bank", 1.0);
        this.priceList.put("Tax to different bank", 1.95);
        this.priceList.put("Exchange to same currency", 1.05);
        this.priceList.put("Exchange to different currency", 1.65);
    }

    public void addAccountToNumberOfCustomers(String id, int numberOfAccounts) {
        numberOfCustomers.put(id, numberOfAccounts);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        if (bankName.isBlank()) {
            throw new InvalidInputException(ExceptionMessage.INVALID_BANK_NAME.getMessage());
        }
        this.bankName = bankName;
    }

    public void setBankAddress(String bankAddress) {
        if (bankAddress.isBlank()) {
            throw new InvalidInputException(ExceptionMessage.INVALID_ADDRESS.getMessage());
        }
        this.bankAddress = bankAddress;
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    public void addTransactions(Transactions transaction) {
        transactions.add(transaction);
    }

    public Map<String, Integer> getNumberOfCustomers() {
        return Collections.unmodifiableMap(numberOfCustomers);
    }

    @Override
    public String toString() {
        return bankName + " bank" + System.lineSeparator() +
                "The address of the bank is: " + bankAddress + System.lineSeparator();
    }
}
