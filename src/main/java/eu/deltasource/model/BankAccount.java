package eu.deltasource.model;

import eu.deltasource.enums.AccountType;
import eu.deltasource.enums.Currency;
import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.exception.*;

import java.time.LocalDateTime;
import java.util.*;

public class BankAccount {

    private BankAccountOwner owner;
    private String iban;
    private String currency;
    private double availableAmount;
    private String bankName;
    private List<Transaction> accountTransactions = new LinkedList<>();
    private List<AccountType> accountTypes = new ArrayList<>();

    public BankAccount(BankAccountOwner owner, String currency, double availableAmount, String... accountType) {
        this.owner = owner;
        this.iban = "BGSF" + usingRandomUUID();
        setCurrency(currency);
        setAvailableAmount(availableAmount);
        setAccountType(accountType);
    }

    /**
     * Prints out the transactions of the account, if there are any, otherwise, returns a message that there are no transactions of the given account.
     *
     * @return
     */
    public String allTransactions() {
        StringBuilder transactions = new StringBuilder();
        List<Transaction> accountTransactions = getAccountTransactions();

        if (accountTransactions.isEmpty()) {
            transactions.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return transactions.toString();
        }

        transactions.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        int numberOfTransactions = 1;
        for (int i = accountTransactions.size() - 1; i >= 0; i--) {
            Transaction currentTransaction = accountTransactions.get(i);
            transactions.append(String.format("Transaction #%d\n", numberOfTransactions++));
            transactions.append(currentTransaction).append("---------------------------------").append(System.lineSeparator());
        }

        return transactions.toString();
    }

    /**
     * Prepares bank statements based on the given time range, if there are transactions in this time range, they will be printed out, otherwise, there won't be
     * any transactions printed.
     *
     * @param start
     * @param end
     */
    public void prepareBankStatement(LocalDateTime start, LocalDateTime end) {

        for (Transaction accountTransaction : accountTransactions) {
            if (accountTransaction.getTimestamp().isBefore(start) || accountTransaction.getTimestamp().isAfter(end)) {
                String message = String.format("No transactions for the account of %s %s for the given period of time.", owner.getFirstName(), owner.getLastName());
                throw new NoTransactionsOfTheGivenAccountException(message);
            }
        }

        printAccountInformation(start, end);
        for (Transaction transaction : getAccountTransactions()) {
            printOutInformationDependingOnTheTransactionType(transaction);
        }
        System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }

    private void printAccountInformation(LocalDateTime start, LocalDateTime end) {
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
        System.out.printf("End date: %d-%d-%d,\n", endDayOfMonth, endMonthValue, endYear);
        System.out.println("Transactions:");
        System.out.println("--------------");
    }

    /**
     * A transaction type can be either deposit, withdraw or a transfer between two bank accounts.
     *
     * @param transaction
     */
    private void printOutInformationDependingOnTheTransactionType(Transaction transaction) {
        int dayOfMonth = transaction.getTimestamp().getDayOfMonth();
        int month = transaction.getTimestamp().getMonthValue();
        int year = transaction.getTimestamp().getYear();

        if (transaction.getAmountTransferred() != 0) {
            System.out.printf("Amount transferred: %.2f\n", transaction.getAmountTransferred());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        } else if (transaction.getAmountDeposited() != 0) {
            System.out.printf("Amount Deposited: %.2f\n", transaction.getAmountDeposited());
            System.out.println("Currency: " + transaction.getSourceCurrency());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        } else if (transaction.getAmountWithdrawn() != 0) {
            System.out.printf("Amount withdrawn: %.2f\n", transaction.getAmountWithdrawn());
            System.out.println("Currency: " + transaction.getSourceCurrency());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        }
        if (transaction.getTargetIban() == null && transaction.getTargetCurrency() == null) {
            System.out.printf("Amount transferred: %.2f\n", transaction.getAmountTransferred());
            System.out.println("Source currency: " + transaction.getSourceCurrency());
            System.out.println("Exchange rate: " + transaction.getExchangeRate());
            System.out.printf("Timestamp: %d-%d-%d\n", dayOfMonth, month, year);
            System.out.println("----------------<");
            return;
        }
        System.out.println("Source account: " + transaction.getSourceIban());
        System.out.println("Target account: " + transaction.getTargetIban());
        System.out.printf("Amount transferred: %.2f\n", transaction.getAmountTransferred());
        System.out.println("Source currency: " + transaction.getSourceCurrency());
        System.out.println("Target currency: " + transaction.getTargetCurrency());
        System.out.println("Exchange rate: " + transaction.getExchangeRate());
        System.out.printf("Timestamp: %d-%d-%d", dayOfMonth, month, year);
        System.out.println("----------------<");
    }

    public void addMoneyToAccountBalance(double amount) {
        availableAmount += amount;
    }

    public void removeMoneyFromAccountBalance(double amount) {
        availableAmount -= amount;
    }

    public void addTransaction(Transaction transaction) {
        accountTransactions.add(transaction);
    }

    /**
     * Checks if the currency of the account is different than the currency the bank offers.
     *
     * @param currency
     * @return
     */
    private boolean isCurrencyValid(String currency) {
        return currency.equals(Currency.BGN.getMessage()) || currency.equals(Currency.USD.getMessage()) || currency.equals(Currency.GBP.getMessage());
    }

    /**
     * Generates a random string of letters and numbers and concatenates the result to the BGSF string afterwards
     *
     * @return
     */
    private String usingRandomUUID() {
        UUID randomUUID = UUID.randomUUID();

        return randomUUID.toString().toUpperCase().replace("-", "");
    }

    public BankAccountOwner getOwner() {
        return owner;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }

    private void setCurrency(String currency) {
        if (!isCurrencyValid(currency)) {
            throw new InvalidCurrencyException(ExceptionMessage.INVALID_CURRENCY.getMessage());
        }
        this.currency = currency;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        if (availableAmount < 0) {
            throw new AvailableAmountCannotBeNegativeException(ExceptionMessage.INVALID_AMOUNT.getMessage());
        }
        this.availableAmount = availableAmount;
    }

    public String getBank() {
        return bankName;
    }
    public void setBank(String bank) {
        this.bankName = bank;
    }

    public List<Transaction> getAccountTransactions() {
        return Collections.unmodifiableList(accountTransactions);
    }

    public List<AccountType> getAccountTypes() {
        return Collections.unmodifiableList(accountTypes);
    }

    private void setAccountType(String... accountType) {
        for (String type : accountType) {
            if (type.equals(AccountType.CURRENT_ACCOUNT.getMessage())) {
                accountTypes.add(AccountType.CURRENT_ACCOUNT);
            } else if (type.equals(AccountType.SAVINGS_ACCOUNT.getMessage())) {
                accountTypes.add(AccountType.SAVINGS_ACCOUNT);
            } else {
                throw new AccountTypeCannotBeDifferentFromCurrentAndSavingsException(ExceptionMessage.INVALID_ACCOUNT_TYPE.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Account name = " + "%s" + " " + "%s" +
                "\nAvailableAmount = " + "%.2f" + " %s\n" +
                "------------------------------\n", owner.getFirstName(), owner.getLastName(), availableAmount, currency);
    }
}
