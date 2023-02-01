package eu.deltasource;

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

    public void withdrawMoneyFromAccount(BankAccount account, double amountToWithdraw, LocalDate date) {
        double moneyAvailable = account.getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new IllegalArgumentException("Not enough money to withdraw.");
        }

        if (!getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't withdraw money from bank %s, because your bank is %s.\n", getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable -= amountToWithdraw;
        }

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateWithdrawTransaction(account, amountToWithdraw, date);
        account.addTransaction(transaction);
    }

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit, LocalDate date) {
        double moneyAvailable = account.getAvailableAmount();

        if (!getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't deposit from bank %s to bank %s" +
                    ", because they are not the same bank. \n", getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable += amountToDeposit;
        }

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateDepositTransaction(account, amountToDeposit, date);

        account.addTransaction(transaction);
    }

    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit, LocalDate date) {

        checkValidAccounts(sourceAccount, targetAccount, amountToDeposit);

        final double originalAmount = amountToDeposit;

        double fees = 0;

        if (!sourceAccount.getBankInstitution().getBankName().equals(targetAccount.getBankInstitution().getBankName())) {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to same bank");
        }

        double exchangeRate;

        if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
            String sourceAccountCurrency = sourceAccount.getCurrency();
            String targetAccountCurrency = targetAccount.getCurrency();

            amountToDeposit = checkCurrenciesAndCalculateTheAmountToBeDeposited(amountToDeposit, sourceAccountCurrency, targetAccountCurrency);

            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to different currency");
        } else {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to same currency");
        }

        double amountWithTaxes = (originalAmount * exchangeRate) + fees;

        double moneyInSourceAccount = sourceAccount.getAvailableAmount();
        double moneyInTargetAccount = targetAccount.getAvailableAmount();

        if (moneyInSourceAccount < amountWithTaxes) {
            double negativeBalance = moneyInSourceAccount - amountWithTaxes;
            String message = String.format("You don't have enough money for that kind of transaction, " +
                    " otherwise the result of your balance would be %.2f %s\n", negativeBalance, sourceAccount.getCurrency());

            throw new IllegalArgumentException(message);
        }

        moneyInSourceAccount -= amountWithTaxes;

//        double allTAxes = amountWithTaxes - amountToDeposit;

        sourceAccount.setAvailableAmount(moneyInSourceAccount);

        targetAccount.setAvailableAmount(moneyInTargetAccount + originalAmount);

        List<Transactions> sourceAccountTransactions = sourceAccount.getAccountTransactions();
        List<Transactions> targetAccountTransactions = targetAccount.getAccountTransactions();

        if (sourceAccountTransactions.isEmpty()) {
            Transactions sourceAccountTransaction = updateSourceAccountTransaction(sourceAccount, targetAccount, amountWithTaxes, exchangeRate, date);

            sourceAccount.addTransaction(sourceAccountTransaction);
        } else {
            Transactions transaction =  updateSourceAccountTransaction(sourceAccount, targetAccount, amountWithTaxes, exchangeRate, date);
            sourceAccount.addTransaction(transaction);
        }

        if (targetAccountTransactions.isEmpty()) {
            Transactions targetAccountTransactions1 = updateTargetAccountTransactions(sourceAccount, targetAccount, originalAmount, exchangeRate, date);

            targetAccount.addTransaction(targetAccountTransactions1);
        } else {
            Transactions transaction = updateTargetAccountTransactions(sourceAccount, targetAccount, originalAmount, exchangeRate, date);
            targetAccount.addTransaction(transaction);
        }

    }

    private void checkValidAccounts(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        if (!sourceAccount.getAccountType().equals("CurrentAccount") || !targetAccount.getAccountType().equals("CurrentAccount")) {
            throw new IllegalArgumentException("Transfers are only allowed between two Current accounts.\n");
        }

        if (sourceAccount.getIban().equals(targetAccount.getIban())) {
            throw new IllegalArgumentException("It is not allowed to transfer money to the same bank account.\n");
        }

        if (sourceAccount.getAvailableAmount() < amountToDeposit) {
            throw new IllegalArgumentException("Not enough money in the source account.\n");
        }

        if (!getBankName().equals(sourceAccount.getBankInstitution().getBankName())) {
            throw new IllegalArgumentException(String.format("Source account's bank is %s, not %s.\n", sourceAccount.getBankInstitution().getBankName(), getBankName()));
        }
    }

    private double checkCurrenciesAndCalculateTheAmountToBeDeposited(double amountToDeposit, String sourceAccountCurrency, String targetAccountCurrency) {
        if (sourceAccountCurrency.equals("BGN")) {
            if (targetAccountCurrency.equals("USD")) {
                amountToDeposit = amountToDeposit * 0.55;
            } else if (targetAccountCurrency.equals("GBP")) {
                amountToDeposit = amountToDeposit * 0.45;
            }
        } else if (sourceAccountCurrency.equals("USD")) {
            if (targetAccountCurrency.equals("BGN")) {
                amountToDeposit = amountToDeposit * 1.80;
            } else if (targetAccountCurrency.equals("GBP")) {
                amountToDeposit = amountToDeposit * 0.80;
            }
        } else if (sourceAccountCurrency.equals("GBP")) {
            if (targetAccountCurrency.equals("BGN")) {
                amountToDeposit = amountToDeposit * 2.23;
            } else if (targetAccountCurrency.equals("USD")) {
                amountToDeposit = amountToDeposit * 1.24;
            }
        }
        return amountToDeposit;
    }

    private Transactions updateSourceAccountTransaction(BankAccount sourceAccount, BankAccount targetAccount, double amount, double exchangeRate, LocalDate date) {
        Transactions transaction = new Transactions();
        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setAmountTransferred(amount);

        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());

        transaction.setTimestamp(date);
        return transaction;
    }

    private Transactions updateTargetAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount, double amount, double exchangeRate, LocalDate date) {
        Transactions transaction = new Transactions();
        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setAmountTransferred(amount);

        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());

        transaction.setTimestamp(date);
        return transaction;
    }

    private Transactions updateDepositTransaction(BankAccount account, double amount, LocalDate date) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountDeposited(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());
        transaction.setTimestamp(date);

        return transaction;
    }

    private Transactions updateWithdrawTransaction(BankAccount account, double amount, LocalDate date) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountWithdrawn(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());
        transaction.setTimestamp(date);

        return transaction;
    }

    public String getBankName() {
        return bankName;
    }

    private Map<String, Double> getPriceList() {
        return priceList;
    }

    public Map<String, Integer> getNumberOfCustomers() {
        return this.numberOfCustomers;
    }

    @Override
    public String toString() {
        return "" + bankName + " bank" + System.lineSeparator() +
                "The address of the bank is: " + bankAddress + System.lineSeparator() +
                "Number of customers of the bank: " + numberOfCustomers.size();
    }

}
