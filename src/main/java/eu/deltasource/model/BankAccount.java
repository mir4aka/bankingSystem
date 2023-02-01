package eu.deltasource.model;

import eu.deltasource.AccountTypes;
import eu.deltasource.exception.AlreadyExistingIdException;
import eu.deltasource.exception.AccountTypeCannotBeDifferentFromCurrentAndSavingsException;
import eu.deltasource.exception.InvalidCurrencyException;
import eu.deltasource.exception.InvalidInputException;

import java.time.LocalDate;
import java.util.*;

public class BankAccount {

    private Owner owner;
    private String iban;
    private String currency;
    private List<AccountTypes> accountTypes;
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
        this.accountTypes = new ArrayList<>();

        try {
            setAccountType(accountType);
//            assignsAccountTypeToAccount(accountType);
            addsAccountToBank();
            setCurrency(currency);
        } catch (AlreadyExistingIdException | IllegalArgumentException |
                 AccountTypeCannotBeDifferentFromCurrentAndSavingsException | InvalidCurrencyException e) {
            System.out.println(e.getMessage());
        }
    }

    private void assignsAccountTypeToAccount(String accountType) {
        if (getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT) && getAccountTypes().contains(AccountTypes.SAVINGS_ACCOUNT)) {
            throw new AlreadyExistingIdException("You already have two accounts. (Current and Savings)\n");
        }
        assignAccounts(accountType);
    }

    private void assignAccounts(String accountType) {
        accountTypes.add(AccountTypes.valueOf(accountType));
    }

    private void addsAccountToBank() {
        Map<String, Integer> numberOfCustomers = getBankInstitution().getNumberOfCustomers();

        if (numberOfCustomers.containsKey(owner.getId())) {
            throw new AlreadyExistingIdException("A person with that id already has an account in this bank.\n");
        }
        numberOfCustomers.putIfAbsent(owner.getId(), 1);
    }

    public String allTransactions() {
        StringBuilder transactions = new StringBuilder();
        List<Transactions> accountTransactions = getAccountTransactions();

        if (accountTransactions.isEmpty()) {
            transactions.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return transactions.toString();
        }

        transactions.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        int numberOfTransactions = 1;
        for (int i = accountTransactions.size() - 1; i >= 0; i--) {
            Transactions currentTransaction = accountTransactions.get(i);
            transactions.append(String.format("Transaction #%d\n", numberOfTransactions++));
            transactions.append(currentTransaction).append("---------------------------------").append(System.lineSeparator());
        }

        return transactions.toString();
    }

    public void prepareBankStatement(LocalDate start, LocalDate end) {
        printAccountInformation(start, end);
        for (Transactions transaction : getAccountTransactions()) {
            LocalDate transactionDate = transaction.getTimestamp();
            if (transactionDate.isAfter(start) && transactionDate.isBefore(end)) {
                checkIfTheTransactionIsADepositWithdrawOrATransfer(transaction);
            } else {
                System.out.println("------");
                System.out.println("No transactions for the account of " + owner.getFirstName() + " " + owner.getLastName() + " for the given period of time.");
                System.out.println("------");
            }
        }
    }

    private void printAccountInformation(LocalDate start, LocalDate end) {
        int startDayOfMonth = start.getDayOfMonth();
        int startMonthValue = start.getMonthValue();
        int startYear = start.getYear();

        int endDayOfMonth = end.getDayOfMonth();
        int endMonthValue = end.getMonthValue();
        int endYear = end.getYear();

        System.out.println("---------------------------------");
        System.out.println("Bank statement for account with IBAN: " + getIban());
        System.out.println("Account owner: \n" + getOwner());
        System.out.println("Currency: " + getCurrency());
        System.out.printf("Start date: %d-%d-%d\n", startDayOfMonth, startMonthValue, startYear);
        System.out.printf("End date: %d-%d-%d\n", endDayOfMonth, endMonthValue, endYear);
        System.out.println("---------------------------------");
        System.out.println("Transactions:");
    }

    private void checkIfTheTransactionIsADepositWithdrawOrATransfer(Transactions transaction) {
        int dayOfMonth = transaction.getTimestamp().getDayOfMonth();
        int month = transaction.getTimestamp().getMonthValue();
        int year = transaction.getTimestamp().getYear();

        if (transaction.getAmountTransferred() != 0) {
            System.out.println("Source account: " + transaction.getSourceIban());
            System.out.println("Amount transferred: " + transaction.getAmountTransferred());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        } else if (transaction.getAmountDeposited() != 0) {
            System.out.println("Source account: " + transaction.getSourceIban());
            System.out.println("Amount Deposited: " + transaction.getAmountDeposited());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        } else if (transaction.getAmountWithdrawn() != 0) {
            System.out.println("Source account: " + transaction.getSourceIban());
            System.out.println("Amount withdrawn: " + transaction.getAmountWithdrawn());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        }
        if (transaction.getTargetIban() == null && transaction.getTargetCurrency() == null) {
            System.out.println("Source account: " + transaction.getSourceIban());
            System.out.println("Amount transferred: " + transaction.getAmountTransferred());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        }
        System.out.println("Source account: " + transaction.getSourceIban());
        System.out.println("Target account: " + transaction.getTargetIban());
        System.out.println("Amount transferred: " + transaction.getAmountTransferred());
        System.out.println("Source currency: " + transaction.getSourceCurrency());
        System.out.println("Target currency: " + transaction.getTargetCurrency());
        System.out.println("Exchange rate: " + transaction.getExchangeRate());
        System.out.printf("Timestamp: %d-%d-%d", dayOfMonth, month, year);
        System.out.println("----------------<");
    }

    public Owner getOwner() {
        return owner;
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

    private void setCurrency(String currency) {
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

    public List<AccountTypes> getAccountTypes() {
        if (accountTypes.size() == 0) {
            throw new InvalidInputException("No account types of this bank account.");
        }
        return Collections.unmodifiableList(accountTypes);
    }

    private void setAccountType(String accountType) {
        if (accountType.equals(AccountTypes.CURRENT_ACCOUNT.getMessage())) {
            accountTypes.add(AccountTypes.CURRENT_ACCOUNT);
        } else if (accountType.equals(AccountTypes.SAVINGS_ACCOUNT.getMessage())) {
            accountTypes.add(AccountTypes.SAVINGS_ACCOUNT);
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
