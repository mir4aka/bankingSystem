package eu.deltasource;

import eu.deltasource.exception.*;
import eu.deltasource.model.BankAccount;
import eu.deltasource.model.Transactions;

import java.time.LocalDate;
import java.util.List;

/**
 * This is the bank service class which is called if a transaction is going to be fulfilled(deposit, withdraw,transfer).
 */
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

    /**
     * The method below takes care of the transaction between two bank accounts
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amountToTransfer
     * @param date
     */
    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToTransfer, LocalDate date) {
        transfer(sourceAccount, targetAccount, amountToTransfer, date);
    }

    /**
     * This is the logic for the transfer method.
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amountToTransfer
     * @param date
     */
    private void transfer(BankAccount sourceAccount, BankAccount targetAccount, double amountToTransfer, LocalDate date) {
        checkValidAccounts(sourceAccount, targetAccount, amountToTransfer);

        double fees = calculateFees(sourceAccount, targetAccount);

        double exchangeRate = calculateExchangeRate(sourceAccount, targetAccount);

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

        if (account.getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT) || account.getAccountTypes().contains(AccountTypes.SAVINGS_ACCOUNT)) {
            moneyAvailable += amountToDeposit;
        }
        return moneyAvailable;
    }

    private double decreaseTheAmountOfMoneyInTheAccount(BankAccount account, double amountToWithdraw) {
        double moneyAvailable = account.getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new NotEnoughMoneyToWithdrawException("Not enough money to withdraw.");
        }

        if (account.getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT) || account.getAccountTypes().contains(AccountTypes.SAVINGS_ACCOUNT)) {
            moneyAvailable -= amountToWithdraw;
        }
        return moneyAvailable;
    }

    /**
     * This method calculates the fee of the amount to be transferred whether it's to a different bank from the source bank or not and returns the value.
     *
     * @param sourceAccount
     * @param targetAccount
     * @return
     */
    private double calculateFees(BankAccount sourceAccount, BankAccount targetAccount) {
        double fees = 0;

        if (!sourceAccount.getBankInstitution().getBankName().equals(targetAccount.getBankInstitution().getBankName())) {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to different bank");
        } else {
            fees += sourceAccount.getBankInstitution().getPriceList().get("Tax to same bank");
        }
        return fees;
    }

    /**
     * This method calculates the exchange rate between banks and returns the value.
     *
     * @param sourceAccount
     * @param targetAccount
     * @return
     */
    private double calculateExchangeRate(BankAccount sourceAccount, BankAccount targetAccount) {
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

    /**
     * This method creates a new transaction which is being updated on both accounts - source and target.
     * This way I can keep up with the transactions and store them in a collection.
     *
     * @param sourceAccount
     * @param targetAccount
     * @param date
     * @param amountToBeDepositedToTargetAccount
     * @param exchangeRate
     * @param amountToBeWithdrawnFromSourceAccount
     */
    private void updatingSourceAccountAndTargetAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount, LocalDate date, double amountToBeDepositedToTargetAccount, double exchangeRate, double amountToBeWithdrawnFromSourceAccount) {
        List<Transactions> sourceAccountTransactions = sourceAccount.getAccountTransactions();
        List<Transactions> targetAccountTransactions = targetAccount.getAccountTransactions();

        if (sourceAccountTransactions.isEmpty()) {
            Transactions sourceAccountTransaction = updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, date);
            sourceAccount.addTransaction(sourceAccountTransaction);
        } else {
            Transactions newTransaction = updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, date);
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

    /**
     * This method takes care of the deposit transaction and adds the transaction to the same collection as the method above.
     *
     * @param account
     * @param amount
     * @param date
     * @return
     */
    private Transactions updateDepositTransaction(BankAccount account, double amount, LocalDate date) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountDeposited(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());
        transaction.setTimestamp(date);

        return transaction;
    }

    /**
     * This method takes care of the withdraw transaction and adds the transaction to the same collection as the method above.
     *
     * @param account
     * @param amount
     * @param date
     * @return
     */
    private Transactions updateWithdrawTransaction(BankAccount account, double amount, LocalDate date) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountWithdrawn(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());
        transaction.setTimestamp(date);

        return transaction;
    }

    /**
     * This method updates source's account transactions (exchange rate, source bank, target bank, etc...)
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amount
     * @param exchangeRate
     * @param date
     * @return
     */
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

    /**
     * This method updates target's account transactions (exchange rate, source bank, target bank, etc...)
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amount
     * @param exchangeRate
     * @param date
     * @return
     */
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

    /**
     * This method checks if the source account's balance is insufficient for a transfer, if the customer is trying to transfer money to his own bank account,
     * if the customer is trying to transfer money between accounts different from `Current Account`.
     * @param sourceAccount
     * @param targetAccount
     * @param amountToDeposit
     */
    private void checkValidAccounts(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        if (!sourceAccount.getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT) || !targetAccount.getAccountTypes().contains(AccountTypes.CURRENT_ACCOUNT)) {
            throw new TransfersAllowedBetweenCurrentAccountsException("Transfers are only allowed between two Current accounts.\n");
        }

        if (sourceAccount.getIban().equals(targetAccount.getIban())) {
            throw new NotAllowedToTransferToTheSameBankAccountException("It is not allowed to transfer money to the same bank account.\n");
        }

        if (sourceAccount.getAvailableAmount() < amountToDeposit) {
            throw new NotEnoughMoneyInTheSourceAccountException("Not enough money in the source account.\n");
        }
    }

    /**
     * This method checks if the source account and target account's currencies are the same or not. Therefore, the amountToDeposit is being calculated
     * based on the result.
     * @param amountToDeposit
     * @param sourceAccountCurrency
     * @param targetAccountCurrency
     * @return
     */
    private double checkCurrenciesAndCalculateTheAmountToBeDeposited(double amountToDeposit, String sourceAccountCurrency, String targetAccountCurrency) {
        if (sourceAccountCurrency.equals("BGN")) {
            if (targetAccountCurrency.equals("USD")) {
                amountToDeposit = amountToDeposit * 0.55;
            } else if (targetAccountCurrency.equals("GBP")) {
                amountToDeposit = amountToDeposit * 0.45;
            } else if (targetAccountCurrency.equals("BGN")){
                amountToDeposit = amountToDeposit * 1;
            }
        } else if (sourceAccountCurrency.equals("USD")) {
            if (targetAccountCurrency.equals("BGN")) {
                amountToDeposit = amountToDeposit * 1.80;
            } else if (targetAccountCurrency.equals("GBP")) {
                amountToDeposit = amountToDeposit * 0.80;
            } else if (targetAccountCurrency.equals("USD")) {
                amountToDeposit = amountToDeposit * 1;
            }
        } else if (sourceAccountCurrency.equals("GBP")) {
            if (targetAccountCurrency.equals("BGN")) {
                amountToDeposit = amountToDeposit * 2.23;
            } else if (targetAccountCurrency.equals("USD")) {
                amountToDeposit = amountToDeposit * 1.24;
            } else if (targetAccountCurrency.equals("GBP")) {
                amountToDeposit = amountToDeposit * 1;
            }
        }
        return amountToDeposit;
    }

}
