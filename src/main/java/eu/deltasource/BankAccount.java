package eu.deltasource;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankAccount {
    private Owner owner;
    private BankInstitution bankInstitution;
    private Transactions transactions;
    private String iban;
    private String currency;
    private double availableAmount;
    private final String accountType;

    public BankAccount(Owner owner, String ownerId, BankInstitution bankInstitution, String iban, String currency, double availableAmount, String accountType){
        this.owner = owner;
        this.owner.setId(ownerId);
        this.bankInstitution = bankInstitution;
        this.iban = iban;
        this.setCurrency(currency);
        this.availableAmount = availableAmount;
        this.accountType = accountType;
        this.transactions = new Transactions();

        try {
            assignsAccountTypeToAccount(accountType);
            addsAccountToBank();
        } catch (AlreadyExistingIdException e) {
            System.out.println(e.getMessage());
        }
    }

    private void assignsAccountTypeToAccount(String accountType) {

        if (this.owner.getAccountTypes().contains("CurrentAccount") && this.owner.getAccountTypes().contains("SavingsAccount")) {
            throw new AlreadyExistingIdException("You already have two accounts. (Current and Savings)\n");
        }

        this.owner.assignAccounts(accountType);
    }

    private void addsAccountToBank() {
        Map<String, Integer> numberOfCustomers = this.getBankInstitution().getNumberOfCustomers();

        if (numberOfCustomers.containsKey(owner.getId())) {
            throw new AlreadyExistingIdException("A person with that id already has an account in this bank.");
        }

        numberOfCustomers.putIfAbsent(owner.getId(), 1);
    }

    public String allTransactions() {
        StringBuilder sb = new StringBuilder();

        Map<String, Timestamp> amountTransferred = getTransactions().getAmountTransferred();

        if (amountTransferred.isEmpty()) {
            sb.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return sb.toString();
        }

        sb.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        for (Map.Entry<String, Timestamp> transaction : amountTransferred.entrySet()) {
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(transaction.getValue());

            sb.append(transaction.getKey()).append(" on ").append(timeStamp).append(System.lineSeparator());
        }

        return sb.toString();
    }

//    public void withdrawMoneyFromAccount(double amountToWithdraw) {
//        double moneyAvailable = getAvailableAmount();
//
//        if (moneyAvailable < amountToWithdraw) {
//            throw new IllegalArgumentException("Not enough money to withdraw");
//        }
//
//        if (getAccountType().equals("CurrentAccount") || getAccountType().equals("SavingsAccount")) {
//            moneyAvailable -= amountToWithdraw;
//        }
//
//        setAvailableAmount(moneyAvailable);
//        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();
//        amountTransferred.push(String.format("Withdrawn money -> %.2f lv.", amountToWithdraw));
//
//        getTransactions().setAmountTransferred(amountTransferred);
//    }
//
//    public void depositMoneyToAccount(double amountToDeposit) {
//        double moneyAvailable = getAvailableAmount();
//
//        if (getAccountType().equals("CurrentAccount") || getAccountType().equals("SavingsAccount")) {
//            moneyAvailable += amountToDeposit;
//        }
//
//        setAvailableAmount(moneyAvailable);
//
//        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();
//        amountTransferred.push(String.format("Deposited money -> %.2f lv.", amountToDeposit));
//
//        getTransactions().setAmountTransferred(amountTransferred);
//    }

//    public void transferMoney(BankAccount targetAccount, double amountToDeposit) {
//        double fees = 0;
//
//        double exchangeRate;
//
//        if (!this.accountType.equals("CurrentAccount") || !targetAccount.getAccountType().equals("CurrentAccount")) {
//            throw new IllegalArgumentException("Transfers are only allowed between two Current accounts.\n");
//        }
//
//        if (this.getIban().equals(targetAccount.getIban())) {
//            throw new IllegalArgumentException("It is not allowed to transfer money to the same bank account.\n");
//        }
//
//        if (this.getAvailableAmount() < amountToDeposit) {
//            throw new IllegalArgumentException("Not enough money in the source account.\n");
//        }
//
//        if (!this.getBankInstitution().getBankName().equals(targetAccount.bankInstitution.getBankName())) {
//            fees += this.getBankInstitution().getPriceList().get("Tax to different bank");
//        } else {
//            fees += this.getBankInstitution().getPriceList().get("Tax to same bank");
//        }
//
//        if (!this.currency.equals(targetAccount.getCurrency())) {
//            exchangeRate = this.getBankInstitution().getPriceList().get("Exchange to different bank");
//        } else {
//            exchangeRate = this.getBankInstitution().getPriceList().get("Exchange to same bank");
//        }
//        double finalAmount = amountToDeposit * exchangeRate;
//
//        finalAmount += fees;
//
//        double moneyInMyAccount = getAvailableAmount();
//        moneyInMyAccount -= finalAmount;
//
//        setAvailableAmount(moneyInMyAccount);
//
////        this.withdrawMoneyFromAccount(finalAmount);
//
//        targetAccount.depositMoneyToAccount(amountToDeposit);
//
//        ArrayDeque<String> amountTransferred = transactions.getAmountTransferred();
//        amountTransferred.push(String.format("Amount transferred -> %.2f lv.", amountToDeposit) );
//
//        updateTransactions(targetAccount, exchangeRate);
//    }

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
            throw new IllegalArgumentException("Invalid currency for a bank account. You may select BGN, USD or GBP.");
        }
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public String getAccountType() {
        return accountType;
    }

//    private void updateTransactions(BankAccount sourceAccount, BankAccount targetAccount, double exchangeRate) {
//        sourceAccount.transactions.setExchangeRate(exchangeRate);
//        sourceAccount.transactions.setSourceBank(this.getBankInstitution());
//        sourceAccount.transactions.setTargetBank(targetAccount.getBankInstitution());
//        sourceAccount.transactions.setSourceCurrency(this.getCurrency());
//        sourceAccount.transactions.setTargetCurrency(targetAccount.getCurrency());
//        sourceAccount.transactions.setSourceIban(this.getIban());
//        sourceAccount.transactions.setTargetIban(targetAccount.getIban());
//        sourceAccount.transactions.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
//
//        targetAccount.transactions.setExchangeRate(exchangeRate);
//        targetAccount.transactions.setSourceBank(this.getBankInstitution());
//        targetAccount.transactions.setTargetBank(targetAccount.getBankInstitution());
//        targetAccount.transactions.setSourceCurrency(this.getCurrency());
//        targetAccount.transactions.setTargetCurrency(targetAccount.getCurrency());
//        targetAccount.transactions.setSourceIban(this.getIban());
//        targetAccount.transactions.setTargetIban(targetAccount.getIban());
//        targetAccount.transactions.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
//    }

    @Override
    public String toString() {
        return String.format("Account name = " + "%s" + " " + "%s" +
                "\nAvailableAmount = " + "%.2f" + " lv.\n", owner.getFirstName(), owner.getLastName(), this.availableAmount);
    }
}
