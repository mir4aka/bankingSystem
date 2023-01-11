package eu.deltasource;

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

        if(getAccountType().equals("CurrentAccount") || getAccountType().equals("SavingsAccount")) {
            moneyAvailable += amountToDeposit;
        }

        setAvailableAmount(moneyAvailable);

        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();
        amountTransferred.push(String.format("Deposited money -> %.2f lv.", amountToDeposit));

        getTransactions().setAmountTransferred(amountTransferred);
    }

    public void transferMoney(BankAccount targetAccount, double amount) {
        if (!this.accountType.equals("CurrentAccount") || !targetAccount.getAccountType().equals("SavingsAccount")) {
            throw new IllegalArgumentException("Transfers are only allowed between current accounts");
        }

        if (this.getIban().equals(targetAccount.getIban())) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        if (this.getAvailableAmount() < amount) {
            throw new IllegalArgumentException("Insufficient funds in the source account");
        }

        double fees = 0;

        if (!this.getBankInstitution().getBankName().equals(targetAccount.bankInstitution.getBankName())) {
            fees += this.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += this.getBankInstitution().getPriceList().get("Tax to same bank");
        }

        double exchangeRate = 1.0;

        if (!this.currency.equals(targetAccount.getCurrency())) {
            exchangeRate = this.getBankInstitution().getPriceList().get(this.currency + "Exchange to different bank" + targetAccount.getCurrency());
        }
        double finalAmount = amount * exchangeRate;

        finalAmount += fees;

        this.withdrawMoneyFromAccount(finalAmount);
        targetAccount.depositMoneyToAccount(finalAmount);

        // create and save transaction
        Transactions t = new Transactions();
        ArrayDeque<String> amountTransferred = t.getAmountTransferred();
        amountTransferred.push("Amount transferred -> " + finalAmount);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public BankInstitution getBankInstitution() {
        return bankInstitution;
    }

    public void setBankInstitution(BankInstitution bankInstitution) {
        this.bankInstitution = bankInstitution;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public String getAccountType() {
        return accountType;
    }

    public String allTransactions() {
        StringBuilder sb = new StringBuilder();

        ArrayDeque<String> amountTransferred = getTransactions().getAmountTransferred();

        if(amountTransferred.isEmpty()) {
            sb.append("There are no transactions for the account of ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(System.lineSeparator());
            return sb.toString();
        }

        sb.append("Transactions of account ").append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(": ").append(System.lineSeparator());

        for (String s : amountTransferred) {
            sb.append(s).append(System.lineSeparator());
        }

        return sb.toString();
    }


    @Override
    public String toString() {
        return "Account name = " + owner.getFirstName() + " " + owner.getLastName() +
                "\nAccount type = " + accountType +
                "\nAvailableAmount = " + availableAmount + "\n";
    }
}
