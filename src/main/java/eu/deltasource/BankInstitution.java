package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class BankInstitution {
    private String bankName;
    private String bankAddress;
    private Owner owner = new Owner();
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

        if (!this.getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't withdraw money from bank %s, because your bank is %s.\n", this.getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable -= amountToWithdraw;
        }

        account.setAvailableAmount(moneyAvailable);
        Map<String, Timestamp> amountTransferred = account.getTransactions().getAmountTransferred();
        amountTransferred.put(String.format("Withdrawn money -> -%.2f lv.", amountToWithdraw), Timestamp.valueOf(LocalDateTime.now()));

        account.getTransactions().setAmountTransferred(amountTransferred);
    }

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit) {
        double moneyAvailable = account.getAvailableAmount();

        if (!this.getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't deposit from bank %s to bank %s" +
                    ", because they are not the same bank. \n", this.getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable += amountToDeposit;
        }

        account.setAvailableAmount(moneyAvailable);

        Map<String, Timestamp> amountTransferred = account.getTransactions().getAmountTransferred();
        amountTransferred.put(String.format("Deposited money -> +%.2f lv.", amountToDeposit), Timestamp.valueOf(LocalDateTime.now()));

        account.getTransactions().setAmountTransferred(amountTransferred);
    }

    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {

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
            throw new IllegalArgumentException("Source account's bank is " + sourceAccount.getBankInstitution().getBankName());
        }

        double fees = 0;

        if (!sourceAccount.getBankInstitution().getBankName().equals(targetAccount.getBankInstitution().getBankName())) {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to same bank");
        }

        double exchangeRate;

        if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to different currency");
        } else {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to same currency");
        }
        double finalAmount = amountToDeposit * exchangeRate;

        finalAmount += fees;

        double moneyInMyAccount = sourceAccount.getAvailableAmount();

        if (moneyInMyAccount < finalAmount) {
            double negativeBalance = moneyInMyAccount - finalAmount;
            String message = String.format("You don't have enough money for that kind of transaction, " +
                    " otherwise the result of your balance would be %.2f lv.\n", negativeBalance);

            throw new IllegalArgumentException(message);
        }

        moneyInMyAccount -= finalAmount;

        double allTAxes = finalAmount - amountToDeposit;

        sourceAccount.setAvailableAmount(moneyInMyAccount);

        double availableAmount = targetAccount.getAvailableAmount();
        targetAccount.setAvailableAmount(availableAmount + amountToDeposit);

        Map<String, Timestamp> sourceAccountTransactions = sourceAccount.getTransactions().getAmountTransferred();
        Map<String, Timestamp> targetAccountTransactions = targetAccount.getTransactions().getAmountTransferred();

        sourceAccountTransactions.put(String.format("Amount transferred -> -%.2f lv. (+%.2f lv. Taxes)", amountToDeposit, allTAxes), Timestamp.valueOf(LocalDateTime.now()));
        targetAccountTransactions.put(String.format("Amount transferred -> +%.2f lv.", amountToDeposit),Timestamp.valueOf(LocalDateTime.now()));

        updateTransactions(sourceAccount, targetAccount, exchangeRate);
    }

    private String getBankName() {
        return bankName;
    }

    private Map<String, Double> getPriceList() {
        return priceList;
    }

    public Map<String, Integer> getNumberOfCustomers() {
        return this.numberOfCustomers;
    }

    private void updateTransactions(BankAccount sourceAccount, BankAccount targetAccount, double exchangeRate) {
        sourceAccount.getTransactions().setExchangeRate(exchangeRate);
        sourceAccount.getTransactions().setSourceBank(sourceAccount.getBankInstitution());
        sourceAccount.getTransactions().setTargetBank(targetAccount.getBankInstitution());
        sourceAccount.getTransactions().setSourceCurrency(sourceAccount.getCurrency());
        sourceAccount.getTransactions().setTargetCurrency(targetAccount.getCurrency());
        sourceAccount.getTransactions().setSourceIban(sourceAccount.getIban());
        sourceAccount.getTransactions().setTargetIban(targetAccount.getIban());
        sourceAccount.getTransactions().setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));

        targetAccount.getTransactions().setExchangeRate(exchangeRate);
        targetAccount.getTransactions().setSourceBank(sourceAccount.getBankInstitution());
        targetAccount.getTransactions().setTargetBank(targetAccount.getBankInstitution());
        targetAccount.getTransactions().setSourceCurrency(sourceAccount.getCurrency());
        targetAccount.getTransactions().setTargetCurrency(targetAccount.getCurrency());
        targetAccount.getTransactions().setSourceIban(sourceAccount.getIban());
        targetAccount.getTransactions().setTargetIban(targetAccount.getIban());
        targetAccount.getTransactions().setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public String toString() {
        return "The name of the bank is: " + this.bankName + System.lineSeparator() +
                "The address of the bank is: " + this.bankAddress + System.lineSeparator() +
                "Number of customers of the bank: " + this.numberOfCustomers.size() + System.lineSeparator();
    }

}
