package eu.deltasource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayDeque;

public class BankAccount {
    private Owner owner;
    private BankInstitution bankInstitution;
    private Transactions transactions;
    private String iban;
    private String currency;
    private double availableAmount;
    private final String accountType;

    public BankAccount(Owner owner, BankInstitution bankInstitution, String iban, String currency, double availableAmount, String accountType) {
        this.owner = owner;
        this.bankInstitution = bankInstitution;
        this.iban = iban;
        this.currency = currency;
        this.availableAmount = availableAmount;
        this.accountType = accountType;

        this.transactions = new Transactions();
    }

    public void withdrawMoneyFromAccount(double amountToWithdraw) {
        double moneyAvailable = getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new IllegalArgumentException("Not enough money to withdraw");
        }

        if (getAccountType().equals("CurrentAccount") || getAccountType().equals("SavingsAccount")) {
            moneyAvailable -= amountToWithdraw;
        }

        setAvailableAmount(moneyAvailable);
        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();
        amountTransferred.push(String.format("Withdrawn money -> %.2f lv.", amountToWithdraw));

        getTransactions().setAmountTransferred(amountTransferred);
    }

    public void depositMoneyToAccount(double amountToDeposit) {
        double moneyAvailable = getAvailableAmount();

        if (getAccountType().equals("CurrentAccount") || getAccountType().equals("SavingsAccount")) {
            moneyAvailable += amountToDeposit;
        }

        setAvailableAmount(moneyAvailable);

        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();
        amountTransferred.push(String.format("Deposited money -> %.2f lv.", amountToDeposit));

        getTransactions().setAmountTransferred(amountTransferred);
    }

    public void transferMoney(BankAccount targetAccount, double amountToDeposit) {
        double fees = 0;

        double exchangeRate;

        if (!this.accountType.equals("CurrentAccount") || !targetAccount.getAccountType().equals("CurrentAccount")) {
            throw new IllegalArgumentException("Transfers are only allowed between two Current accounts.\n");
        }

        if (this.getIban().equals(targetAccount.getIban())) {
            throw new IllegalArgumentException("It is not allowed to transfer money to the same bank account.\n");
        }

        if (this.getAvailableAmount() < amountToDeposit) {
            throw new IllegalArgumentException("Not enough money in the source account.\n");
        }

        if (!this.getBankInstitution().getBankName().equals(targetAccount.bankInstitution.getBankName())) {
            fees += this.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += this.getBankInstitution().getPriceList().get("Tax to same bank");
        }

        if (!this.currency.equals(targetAccount.getCurrency())) {
            exchangeRate = this.getBankInstitution().getPriceList().get("Exchange to different bank");
        } else {
            exchangeRate = this.getBankInstitution().getPriceList().get("Exchange to same bank");
        }
        double finalAmount = amountToDeposit * exchangeRate;

        finalAmount += fees;

        double moneyInMyAccount = getAvailableAmount();
        moneyInMyAccount -= finalAmount;

        setAvailableAmount(moneyInMyAccount);

//        this.withdrawMoneyFromAccount(finalAmount);

        targetAccount.depositMoneyToAccount(amountToDeposit);

        ArrayDeque<String> amountTransferred = transactions.getAmountTransferred();
        amountTransferred.push(String.format("Amount transferred -> %.2f lv.", amountToDeposit) );

        updateTransactions(targetAccount, exchangeRate);
    }

    private BankInstitution getBankInstitution() {
        return bankInstitution;
    }

    private String getIban() {
        return iban;
    }

    private String getCurrency() {
        return currency;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    private void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    private Transactions getTransactions() {
        return transactions;
    }

    private String getAccountType() {
        return accountType;
    }

    public String allTransactions() {
        StringBuilder sb = new StringBuilder();

        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();

        if (amountTransferred.isEmpty()) {
            sb.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return sb.toString();
        }

        sb.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        for (String transaction : amountTransferred) {
            sb.append(transaction).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private void updateTransactions(BankAccount targetAccount, double exchangeRate) {
        transactions.setExchangeRate(exchangeRate);
        transactions.setSourceBank(this.getBankInstitution());
        transactions.setTargetBank(targetAccount.getBankInstitution());
        transactions.setSourceCurrency(this.getCurrency());
        transactions.setTargetCurrency(targetAccount.getCurrency());
        transactions.setSourceIban(this.getIban());
        transactions.setTargetIban(targetAccount.getIban());
        transactions.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));

        targetAccount.transactions.setExchangeRate(exchangeRate);
        targetAccount.transactions.setSourceBank(this.getBankInstitution());
        targetAccount.transactions.setTargetBank(targetAccount.getBankInstitution());
        targetAccount.transactions.setSourceCurrency(this.getCurrency());
        targetAccount.transactions.setTargetCurrency(targetAccount.getCurrency());
        targetAccount.transactions.setSourceIban(this.getIban());
        targetAccount.transactions.setTargetIban(targetAccount.getIban());
        targetAccount.transactions.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public String toString() {
        return String.format("Account name = " + "%s" + " " + "%s" +
                "\nAccount type = " + "%s" +
                "\nAvailableAmount = " + "%.2f" + " lv.\n", owner.getFirstName(), owner.getLastName(), this.accountType, this.availableAmount);
    }
}
