package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void withdrawMoneyFromAccount(BankAccount account, double amountToWithdraw) {
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

        String amountTransferred = (String.format("Withdrawn money -> -%.2f %s", amountToWithdraw, account.getCurrency()));

        account.getTransactions().setAmountTransferred(amountToWithdraw);

        account.getAccountTransactions().add(account.getTransactions());
    }

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit) {
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

        String amountTransferred = (String.format("Deposited money -> +%.2f %s", amountToDeposit, account.getCurrency()));

        account.getTransactions().setAmountTransferred(amountToDeposit);
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

        double allTAxes = amountWithTaxes - amountToDeposit;

        sourceAccount.setAvailableAmount(moneyInSourceAccount);

        targetAccount.setAvailableAmount(moneyInTargetAccount + originalAmount);

        List<Transactions> sourceAccountTransactions = sourceAccount.getAccountTransactions();
        List<Transactions> targetAccountTransactions = targetAccount.getAccountTransactions();

        if (sourceAccountTransactions.isEmpty()) {
            Transactions sourceAccountTransaction = sourceAccount.getTransactions();
            sourceAccountTransaction.setAmountTransferred(amountWithTaxes);
            sourceAccountTransactions.add(sourceAccountTransaction);
        } else {
            Transactions transaction = updateTransactions(sourceAccount, targetAccount, exchangeRate, date);
            transaction.setAmountTransferred(amountWithTaxes);

            sourceAccount.getAccountTransactions().add(transaction);

            sourceAccount.getTransactions().setAmountTransferred(amountWithTaxes);

//            sourceAccount.getAccountTransactions().add(sourceAccount.getTransactions());
        }

        if (targetAccountTransactions.isEmpty()) {
            Transactions targetAccountTransactions1 = targetAccount.getTransactions();
            targetAccount.getTransactions().setAmountTransferred(originalAmount);
            targetAccountTransactions.add(targetAccountTransactions1);

        } else {
            targetAccount.getTransactions().setAmountTransferred(originalAmount);

            targetAccount.getAccountTransactions().add(targetAccount.getTransactions());
        }

        updateTransactions(sourceAccount, targetAccount, exchangeRate, date);
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

    private Transactions updateTransactions(BankAccount sourceAccount, BankAccount targetAccount, double exchangeRate, LocalDate date) {
        Transactions transaction = new Transactions();

        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());
        transaction.setTimestamp(date);
        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());
        transaction.setTimestamp(date);

//        sourceAccount.getTransactions().setExchangeRate(exchangeRate);
//        sourceAccount.getTransactions().setSourceBank(sourceAccount.getBankInstitution());
//        sourceAccount.getTransactions().setTargetBank(targetAccount.getBankInstitution());
//
//
//        sourceAccount.getTransactions().setSourceCurrency(sourceAccount.getCurrency());
//        sourceAccount.getTransactions().setTargetCurrency(targetAccount.getCurrency());
//        sourceAccount.getTransactions().setSourceIban(sourceAccount.getIban());
//        sourceAccount.getTransactions().setTargetIban(targetAccount.getIban());
//
//        sourceAccount.getTransactions().setTimestamp(date);
//
//        targetAccount.getTransactions().setExchangeRate(exchangeRate);
//        targetAccount.getTransactions().setSourceBank(sourceAccount.getBankInstitution());
//        targetAccount.getTransactions().setTargetBank(targetAccount.getBankInstitution());
//
//        targetAccount.getTransactions().setSourceCurrency(sourceAccount.getCurrency());
//        targetAccount.getTransactions().setTargetCurrency(targetAccount.getCurrency());
//        targetAccount.getTransactions().setSourceIban(sourceAccount.getIban());
//        targetAccount.getTransactions().setTargetIban(targetAccount.getIban());
//
//        targetAccount.getTransactions().setTimestamp(date);
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
