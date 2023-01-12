package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class BankInstitution {
    private String bankName;
    private String bankAddress;
    private int numberOfCustomers;
    private BankAccount account;
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

    public void withdrawMoneyFromAccount(BankAccount account, double amountToWithdraw) {
        double moneyAvailable = account.getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new IllegalArgumentException("Not enough money to withdraw.");
        }

        if(!this.getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't withdraw money from bank %s, because your bank is %s.\n", this.getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable -= amountToWithdraw;
        }

        account.setAvailableAmount(moneyAvailable);
        ArrayDeque<String> amountTransferred = account.getTransactions().getAmountTransferred();
        amountTransferred.push(String.format("Withdrawn money -> -%.2f lv.", amountToWithdraw));

        account.getTransactions().setAmountTransferred(amountTransferred);
    }

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit) {
        double moneyAvailable = account.getAvailableAmount();

        if(!this.getBankName().equals(account.getBankInstitution().getBankName())) {
            String message = String.format("You can't deposit from bank %s to bank %s" +
                    ", because they are not the same bank. \n", this.getBankName(), account.getBankInstitution().getBankName());
            throw new IllegalArgumentException(message);
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable += amountToDeposit;
        }

        account.setAvailableAmount(moneyAvailable);

        ArrayDeque<String> amountTransferred = account.getTransactions().getAmountTransferred();
        amountTransferred.push(String.format("Deposited money -> +%.2f lv.", amountToDeposit));

        account.getTransactions().setAmountTransferred(amountTransferred);
    }

    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        double fees = 0;

        double exchangeRate;

        if (!sourceAccount.getAccountType().equals("CurrentAccount") || !targetAccount.getAccountType().equals("CurrentAccount")) {
            throw new IllegalArgumentException("Transfers are only allowed between two Current accounts.\n");
        }

        if (sourceAccount.getIban().equals(targetAccount.getIban())) {
            throw new IllegalArgumentException("It is not allowed to transfer money to the same bank account.\n");
        }

        if (sourceAccount.getAvailableAmount() < amountToDeposit) {
            throw new IllegalArgumentException("Not enough money in the source account.\n");
        }

        if (!sourceAccount.getBankInstitution().getBankName().equals(targetAccount.getBankInstitution().getBankName())) {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to same bank");
        }

        if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to different bank");
        } else {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to same bank");
        }
        double finalAmount = amountToDeposit * exchangeRate;

        finalAmount += fees;

        double moneyInMyAccount = sourceAccount.getAvailableAmount();

        if(moneyInMyAccount < finalAmount) {
            double negativeBalance = moneyInMyAccount - finalAmount;
            String message = String.format("You don't have enough money for that kind of transaction, " +
                    " otherwise the result of your balance would be %.2f lv.\n", negativeBalance);

            throw new IllegalArgumentException(message);
        }

        moneyInMyAccount -= finalAmount;

        sourceAccount.setAvailableAmount(moneyInMyAccount);

        double availableAmount = targetAccount.getAvailableAmount();
        targetAccount.setAvailableAmount(availableAmount + amountToDeposit);

        ArrayDeque<String> sourceAccountTransactions = sourceAccount.getTransactions().getAmountTransferred();
        ArrayDeque<String> targetAccountTransactions = targetAccount.getTransactions().getAmountTransferred();

        sourceAccountTransactions.push(String.format("Amount transferred -> -%.2f lv.", amountToDeposit) );
        targetAccountTransactions.push(String.format("Amount transferred -> +%.2f", amountToDeposit));

        updateTransactions(sourceAccount, targetAccount, exchangeRate);
    }

    public String getBankName() {
        return bankName;
    }

    public Map<String, Double> getPriceList() {
        return priceList;
    }

    public BankAccount getAccount() {
        return account;
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
                "Number of customers of the bank: " + this.numberOfCustomers + System.lineSeparator();
    }

}
