package eu.deltasource;

import eu.deltasource.exceptions.AccountTypeCannotBeDifferentFromCurrentAndSavingsException;
import eu.deltasource.exceptions.InvalidCurrencyException;

import java.util.*;

public class BankAccount {
    private Owner owner;
    private String iban;
    private String currency;
    private String accountType;
    private double availableAmount;
    private BankInstitution bankInstitution;
    private Transactions transactions;
    private List<Transactions> accountTransactions = new LinkedList<>();

    public BankAccount(Owner owner, String ownerId, BankInstitution bankInstitution, String iban, String currency, double availableAmount, String accountType) {
        this.owner = owner;
        this.owner.setId(ownerId);
        this.bankInstitution = bankInstitution;
        this.iban = iban;
        this.availableAmount = availableAmount;
        this.transactions = new Transactions();

        try {
            setAccountType(accountType);
            assignsAccountTypeToAccount(accountType);
            addsAccountToBank();
            this.setCurrency(currency);
        } catch (AlreadyExistingIdException | IllegalArgumentException |
                 AccountTypeCannotBeDifferentFromCurrentAndSavingsException | InvalidCurrencyException e) {
            System.out.println(e.getMessage());
        }
    }

    private void assignsAccountTypeToAccount(String accountType) {
        if (owner.getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT.getMessage()) && owner.getAccountTypes().contains(AccountTypes.SAVINGS_ACCOUNT.getMessage())) {
            throw new AlreadyExistingIdException("You already have two accounts. (Current and Savings)\n");
        }

        this.owner.assignAccounts(accountType);
    }

    private void addsAccountToBank() {
        Map<String, Integer> numberOfCustomers = getBankInstitution().getNumberOfCustomers();

        if (numberOfCustomers.containsKey(owner.getId())) {
            throw new AlreadyExistingIdException("A person with that id already has an account in this bank.\n");
        }

        numberOfCustomers.putIfAbsent(owner.getId(), 1);
    }

    public String allTransactions() {
        StringBuilder sb = new StringBuilder();

        List<Transactions> accountTransactions = getAccountTransactions();

        if (accountTransactions.isEmpty()) {
            sb.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return sb.toString();
        }

        sb.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        int numberOfTransactions = 1;
        for (int i = accountTransactions.size() - 1; i >= 0; i--) {
            Transactions currentTransaction = accountTransactions.get(i);
            sb.append(String.format("Transaction #%d\n", numberOfTransactions++));
            sb.append(currentTransaction);
        }

        return sb.toString();
    }

    public BankInstitution getBankInstitution() {
        return bankInstitution;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (currency.equals("BGN") || currency.equals("USD") || currency.equals("GBP")) {
            this.currency = currency;
        } else {
            throw new InvalidCurrencyException("Invalid currency for a bank account. You may select BGN, USD or GBP.");
        }
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public List<Transactions> getAccountTransactions() {
        return Collections.unmodifiableList(accountTransactions);
    }

    public void addTransaction(Transactions transaction) {
        accountTransactions.add(transaction);
    }


    public String getAccountType() {
        if (accountType == null) {
            return "";
        }
        return accountType;
    }

    public void setAccountType(String accountType) {
        if (accountType.equals("CurrentAccount")) {
            this.accountType = accountType;
        } else if (accountType.equals("SavingsAccount")) {
            this.accountType = accountType;
        } else {
            throw new AccountTypeCannotBeDifferentFromCurrentAndSavingsException("Account type can be either `Current` or `Savings` type of account.");
        }
    }

    @Override
    public String toString() {
        return String.format("Account name = " + "%s" + " " + "%s" +
                "\nAvailableAmount = " + "%.2f" + " %s\n" +
                "------------------------------\n", owner.getFirstName(), owner.getLastName(), this.availableAmount, getCurrency());
    }
}
