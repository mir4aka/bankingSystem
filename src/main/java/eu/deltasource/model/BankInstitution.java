package eu.deltasource.model;

import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.enums.PriceList;
import eu.deltasource.exception.AlreadyExistingIdException;
import eu.deltasource.exception.InvalidInputException;

import java.util.*;

public class BankInstitution {

    private String bankName;
    private String bankAddress;
    private Map<String, Integer> numberOfCustomers = new HashMap<>();
    private Map<String, Double> priceList = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

    public BankInstitution(String bankName, String bankAddress) {
        setBankName(bankName);
        setBankAddress(bankAddress);
        addsPricesToPriceListOfTheBank();
    }

    public BankInstitution() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    private void addsPricesToPriceListOfTheBank() {
        this.priceList.put(PriceList.TAX_TO_SAME_BANK.getMessage(), 1.2);
        this.priceList.put(PriceList.TAX_TO_DIFFERENT_BANK.getMessage(), 1.55);
        this.priceList.put(PriceList.EXCHANGE_TO_SAME_CURRENCY.getMessage(), 1.05);
        this.priceList.put(PriceList.EXCHANGE_TO_DIFFERENT_CURRENCY.getMessage(), 1.65);
    }

    /**
     * Adds the customer to the bank if there is no customer with the same id, otherwise an exception is thrown.
     *
     * @param id
     */
    public void addCustomerToBank(String id) {
        if (numberOfCustomers.containsKey(id)) {
            throw new AlreadyExistingIdException("An account with this id already exists in this bank.");
        }

        numberOfCustomers.put(id, 1);
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

    public Map<String, Integer> getNumberOfCustomers() {
        return Collections.unmodifiableMap(numberOfCustomers);
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    @Override
    public String toString() {
        return bankName + " bank" + System.lineSeparator() +
                "The address of the bank is: " + bankAddress + System.lineSeparator();
    }
}
