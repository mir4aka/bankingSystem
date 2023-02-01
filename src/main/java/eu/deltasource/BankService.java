package eu.deltasource;

import eu.deltasource.exceptions.*;

import java.time.LocalDate;
import java.util.List;

public class BankService {

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit, LocalDate date) {
        double moneyAvailable = increaseTheAmountOfMoneyInTheAccount(account, amountToDeposit);

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateDepositTransaction(account, amountToDeposit, date);

        account.addTransaction(transaction);
    }

    public void withdrawMoneyFromAccount(BankAccount account, double amountToWithdraw, LocalDate date) {
        double moneyAvailable = decreaseTheAmountOfMoneyInTheAccount(account, amountToWithdraw);

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateWithdrawTransaction(account, amountToWithdraw, date);
        account.addTransaction(transaction);
    }

    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToTransfer, LocalDate date) {

        checkValidAccounts(sourceAccount, targetAccount, amountToTransfer);

        double fees = calculateFees(sourceAccount, targetAccount);

        double exchangeRate = calculateExchangeRate(sourceAccount, targetAccount, amountToTransfer);

        String sourceAccountCurrency = sourceAccount.getCurrency();
        String targetAccountCurrency = targetAccount.getCurrency();

        double amountToBeDepositedToTheTargetAccount = checkCurrenciesAndCalculateTheAmountToBeDeposited(amountToTransfer, sourceAccountCurrency, targetAccountCurrency);
        double amountToBeWithdrawnFromSourceAccount = (amountToBeDepositedToTheTargetAccount * exchangeRate) + fees;

        double moneyInSourceAccount = sourceAccount.getAvailableAmount();
        double moneyInTargetAccount = targetAccount.getAvailableAmount();

        checkIfSourceAccountHasEnoughMoneyToTransfer(sourceAccount, amountToTransfer, moneyInSourceAccount);

        moneyInSourceAccount -= amountToBeWithdrawnFromSourceAccount;

        sourceAccount.setAvailableAmount(moneyInSourceAccount);

        moneyInTargetAccount += amountToBeDepositedToTheTargetAccount;

        targetAccount.setAvailableAmount(moneyInTargetAccount);

        updatingSourceAccountAndTargetAccountTransactions(sourceAccount, targetAccount, date, amountToBeDepositedToTheTargetAccount, exchangeRate, amountToBeWithdrawnFromSourceAccount);
    }

    private double increaseTheAmountOfMoneyInTheAccount(BankAccount account, double amountToDeposit) {
        double moneyAvailable = account.getAvailableAmount();

        if (account.getAccountType().equals(AccountTypes.CURRENT_ACCOUNT.getMessage()) || account.getAccountType().equals(AccountTypes.SAVINGS_ACCOUNT.getMessage())) {
            moneyAvailable += amountToDeposit;
        }
        return moneyAvailable;
    }

    private double decreaseTheAmountOfMoneyInTheAccount(BankAccount account, double amountToWithdraw) {
        double moneyAvailable = account.getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new NotEnoughMoneyToWithdrawException("Not enough money to withdraw.");
        }

        if (account.getAccountType().equals("CurrentAccount") || account.getAccountType().equals("SavingsAccount")) {
            moneyAvailable -= amountToWithdraw;
        }
        return moneyAvailable;
    }

    private double calculateFees(BankAccount sourceAccount, BankAccount targetAccount) {
        double fees = 0;

        if (!sourceAccount.getBankInstitution().getBankName().equals(targetAccount.getBankInstitution().getBankName())) {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to same bank");
        }
        return fees;
    }

    private double calculateExchangeRate(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        double exchangeRate;

        if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to different currency");
        } else {
            exchangeRate = sourceAccount.getBankInstitution().getPriceList().get("Exchange to same currency");
        }
        return exchangeRate;
    }

    private void checkIfSourceAccountHasEnoughMoneyToTransfer(BankAccount sourceAccount, double amountToTransfer, double moneyInSourceAccount) {
        if (moneyInSourceAccount < amountToTransfer) {
            double negativeBalance = moneyInSourceAccount - amountToTransfer;
            String message = String.format("You don't have enough money for that kind of transaction, " +
                    " otherwise the result of your balance would be %.2f %s\n", negativeBalance, sourceAccount.getCurrency());

            throw new NotEnoughMoneyToTransferException(message);
        }
    }

    private void updatingSourceAccountAndTargetAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount, LocalDate date, double amountToBeDepositedToTargetAccount, double exchangeRate, double amountToBeWithdrawnFromSourceAccount) {
        List<Transactions> sourceAccountTransactions = sourceAccount.getAccountTransactions();
        List<Transactions> targetAccountTransactions = targetAccount.getAccountTransactions();

        if (sourceAccountTransactions.isEmpty()) {
            Transactions sourceAccountTransaction = updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, date);
            sourceAccount.addTransaction(sourceAccountTransaction);
        } else {
            Transactions newTransaction =  updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, date);
            sourceAccount.addTransaction(newTransaction);
        }

        if (targetAccountTransactions.isEmpty()) {
            Transactions targetAccountTransaction = updateTargetAccountTransactions(sourceAccount, targetAccount, amountToBeDepositedToTargetAccount, exchangeRate, date);
            targetAccount.addTransaction(targetAccountTransaction);
        } else {
            Transactions newTransaction = updateTargetAccountTransactions(sourceAccount, targetAccount, amountToBeDepositedToTargetAccount, exchangeRate, date);
            targetAccount.addTransaction(newTransaction);
        }
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

    private Transactions updateSourceAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount, double amount, double exchangeRate, LocalDate date) {
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

    private void checkValidAccounts(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        if (!sourceAccount.getAccountType().equals("CurrentAccount") || !targetAccount.getAccountType().equals("CurrentAccount")) {
            throw new TransfersAllowedBetweenCurrentAccountsException("Transfers are only allowed between two Current accounts.\n");
        }

        if (sourceAccount.getIban().equals(targetAccount.getIban())) {
            throw new NotAllowedToTransferToTheSameBankAccountException("It is not allowed to transfer money to the same bank account.\n");
        }

        if (sourceAccount.getAvailableAmount() < amountToDeposit) {
            throw new NotEnoughMoneyInTheSourceAccountException("Not enough money in the source account.\n");
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

}
