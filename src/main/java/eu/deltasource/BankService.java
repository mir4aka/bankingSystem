package eu.deltasource;

import eu.deltasource.enums.AccountType;
import eu.deltasource.enums.Currency;
import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.enums.PriceList;
import eu.deltasource.exception.*;
import eu.deltasource.model.BankAccount;
import eu.deltasource.model.Transactions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//TODO
// read about; replace conditional with polymorphism

/**
 * This is the bank service class which is called if a transaction is going to be fulfilled(deposit, withdraw,transfer).
 */
public class BankService {

    public void depositMoneyToAccount(BankAccount account, double amountToDeposit, LocalDateTime date) {
        checkIfTheDateTimeIsValid(date);

        double moneyAvailable = increaseTheAmountOfMoneyInTheAccount(account, amountToDeposit);

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateDepositTransaction(account, amountToDeposit);

        account.addTransaction(transaction);
    }

    public void withdrawMoneyFromAccount(BankAccount account, double amountToWithdraw, LocalDateTime date) {

        checkIfTheDateTimeIsValid(date);

        double moneyAvailable = decreaseTheAmountOfMoneyInTheAccount(account, amountToWithdraw);

        account.setAvailableAmount(moneyAvailable);

        Transactions transaction = updateWithdrawTransaction(account, amountToWithdraw);

        account.addTransaction(transaction);
    }

    private void checkIfTheDateTimeIsValid(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("You cannot enter a transaction time before.");
        }
    }

    /**
     * The method below takes care of the transaction between two bank accounts
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amountToTransfer
     * @param date
     */
    public void transferMoney(BankAccount sourceAccount, BankAccount targetAccount, double amountToTransfer, LocalDateTime date) {
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
    private void transfer(BankAccount sourceAccount, BankAccount targetAccount, double amountToTransfer, LocalDateTime date) {
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

        sourceAccount.setAvailableAmount(Double.parseDouble(String.format("%.2f", moneyInSourceAccount)));

        moneyInTargetAccount += amountToBeDepositedToTheTargetAccount;

        targetAccount.setAvailableAmount(Double.parseDouble(String.format("%.2f", moneyInTargetAccount)));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatter.format(LocalDateTime.now());
        LocalDateTime timeOfTransaction = LocalDateTime.parse(format, dateTimeFormatter);

        updatingSourceAccountAndTargetAccountTransactions(sourceAccount, targetAccount, timeOfTransaction, amountToBeDepositedToTheTargetAccount, exchangeRate, amountToBeWithdrawnFromSourceAccount);
    }

    private double increaseTheAmountOfMoneyInTheAccount(BankAccount account, double amountToDeposit) {
        if (account.getAccountTypes().contains(AccountType.CURRENT_ACCOUNT) || account.getAccountTypes().contains(AccountType.SAVINGS_ACCOUNT)) {
            account.addAmount(amountToDeposit);
        }
        return account.getAvailableAmount();
    }

    private double decreaseTheAmountOfMoneyInTheAccount(BankAccount account, double amountToWithdraw) {
        double moneyAvailable = account.getAvailableAmount();

        if (moneyAvailable < amountToWithdraw) {
            throw new NotEnoughMoneyToWithdrawException(ExceptionMessage.NOT_ENOUGH_MONEY.getMessage());
        }

        if (account.getAccountTypes().contains(AccountType.CURRENT_ACCOUNT) || account.getAccountTypes().contains(AccountType.SAVINGS_ACCOUNT)) {
            account.removeAmount(amountToWithdraw);

        }
        return account.getAvailableAmount();
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

        if (!sourceAccount.getBankInstitutionName().equals(targetAccount.getBankInstitutionName())) {
            fees += sourceAccount.getBankInstitutionPriceList().get(PriceList.TAX_TO_DIFFERENT_BANK.getMessage());
        } else {
            fees += sourceAccount.getBankInstitutionPriceList().get(PriceList.TAX_TO_SAME_BANK.getMessage());
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
            exchangeRate = sourceAccount.getBankInstitutionPriceList().get(PriceList.EXCHANGE_TO_DIFFERENT_CURRENCY.getMessage());
        } else {
            exchangeRate = sourceAccount.getBankInstitutionPriceList().get(PriceList.EXCHANGE_TO_SAME_CURRENCY.getMessage());
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
    private void updatingSourceAccountAndTargetAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount,
                                                                   LocalDateTime date, double amountToBeDepositedToTargetAccount,
                                                                   double exchangeRate, double amountToBeWithdrawnFromSourceAccount) {
        List<Transactions> sourceAccountTransactions = sourceAccount.getAccountTransactions();
        List<Transactions> targetAccountTransactions = targetAccount.getAccountTransactions();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatter.format(date);
        LocalDateTime dateOfTransaction = LocalDateTime.parse(format, dateTimeFormatter);

        if (sourceAccountTransactions.isEmpty()) {
            Transactions sourceAccountTransaction = updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, dateOfTransaction);
            sourceAccount.addTransaction(sourceAccountTransaction);
        } else {
            Transactions newTransaction = updateSourceAccountTransactions(sourceAccount, targetAccount, amountToBeWithdrawnFromSourceAccount, exchangeRate, dateOfTransaction);
            sourceAccount.addTransaction(newTransaction);
        }

        if (targetAccountTransactions.isEmpty()) {
            Transactions targetAccountTransaction = updateTargetAccountTransactions(sourceAccount, targetAccount, amountToBeDepositedToTargetAccount, exchangeRate, dateOfTransaction);
            targetAccount.addTransaction(targetAccountTransaction);
        } else {
            Transactions newTransaction = updateTargetAccountTransactions(sourceAccount, targetAccount, amountToBeDepositedToTargetAccount, exchangeRate, dateOfTransaction);
            targetAccount.addTransaction(newTransaction);
        }
    }

    /**
     * This method takes care of the deposit transaction and adds the transaction to the same collection as the method above.
     *
     * @param account
     * @param amount
     * @return
     */
    private Transactions updateDepositTransaction(BankAccount account, double amount) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountDeposited(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatter.format(LocalDateTime.now());
        LocalDateTime timeOfTransaction = LocalDateTime.parse(format, dateTimeFormatter);

        transaction.setTimestamp(timeOfTransaction);

        return transaction;
    }

    /**
     * This method takes care of the withdraw transaction and adds the transaction to the same collection as the method above.
     *
     * @param account
     * @param amount
     * @return
     */
    private Transactions updateWithdrawTransaction(BankAccount account, double amount) {
        Transactions transaction = new Transactions();

        transaction.setSourceBank(account.getBankInstitution());
        transaction.setAmountWithdrawn(amount);
        transaction.setSourceCurrency(account.getCurrency());
        transaction.setSourceIban(account.getIban());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatter.format(LocalDateTime.now());
        LocalDateTime timeOfTransaction = LocalDateTime.parse(format, dateTimeFormatter);

        transaction.setTimestamp(timeOfTransaction);

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
    private Transactions updateSourceAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount,
                                                         double amount, double exchangeRate, LocalDateTime date) {
        Transactions transaction = new Transactions();
        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setAmountTransferred(amount);
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());

        DateTimeFormatter dateTimeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatted.format(date);
        LocalDateTime timeOfTransaction = LocalDateTime.parse(format, dateTimeFormatted);

        transaction.setTimestamp(timeOfTransaction);
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
    private Transactions updateTargetAccountTransactions(BankAccount sourceAccount, BankAccount targetAccount,
                                                         double amount, double exchangeRate, LocalDateTime date) {
        Transactions transaction = new Transactions();
        transaction.setExchangeRate(exchangeRate);
        transaction.setSourceBank(sourceAccount.getBankInstitution());
        transaction.setTargetBank(targetAccount.getBankInstitution());
        transaction.setAmountTransferred(amount);
        transaction.setSourceCurrency(sourceAccount.getCurrency());
        transaction.setTargetCurrency(targetAccount.getCurrency());
        transaction.setSourceIban(sourceAccount.getIban());
        transaction.setTargetIban(targetAccount.getIban());

        DateTimeFormatter dateTimeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String format = dateTimeFormatted.format(date);
        LocalDateTime timeOfTransaction = LocalDateTime.parse(format, dateTimeFormatted);

        transaction.setTimestamp(timeOfTransaction);
        return transaction;
    }

    /**
     * This method checks if the source account's balance is insufficient for a transfer, if the customer is trying to transfer money to his own bank account,
     * if the customer is trying to transfer money between accounts different from `Current Account`.
     *
     * @param sourceAccount
     * @param targetAccount
     * @param amountToDeposit
     */
    private void checkValidAccounts(BankAccount sourceAccount, BankAccount targetAccount, double amountToDeposit) {
        if (sourceAccount.getAvailableAmount() < amountToDeposit) {
            throw new NotEnoughMoneyInTheSourceAccountException("Not enough money in the source account.\n");
        }

        if (!sourceAccount.getAccountTypes().contains(AccountType.CURRENT_ACCOUNT) || !targetAccount.getAccountTypes().contains(AccountType.CURRENT_ACCOUNT)) {
            throw new TransfersAllowedBetweenCurrentAccountsException("Transfers are only allowed between two Current accounts.\n");
        }

        if (sourceAccount.getIban().equals(targetAccount.getIban())) {
            throw new NotAllowedToTransferToTheSameBankAccountException("It is not allowed to transfer money to the same bank account.\n");
        }
    }

    /**
     * This method checks if the source account and target account's currencies are the same or not. Therefore, the amountToDeposit is being calculated
     * based on the result.
     *
     * @param amountToDeposit
     * @param sourceAccountCurrency
     * @param targetAccountCurrency
     * @return
     */
    private double checkCurrenciesAndCalculateTheAmountToBeDeposited(double amountToDeposit, String sourceAccountCurrency, String targetAccountCurrency) {
        if (sourceAccountCurrency.equals(Currency.BGN.getMessage())) {
            if (targetAccountCurrency.equals(Currency.USD.getMessage())) {
                amountToDeposit = amountToDeposit * 0.55;
            } else if (targetAccountCurrency.equals(Currency.GBP.getMessage())) {
                amountToDeposit = amountToDeposit * 0.45;
            } else if (targetAccountCurrency.equals(Currency.BGN.getMessage())) {
                amountToDeposit = amountToDeposit * 1;
            }
        } else if (sourceAccountCurrency.equals(Currency.USD.getMessage())) {
            if (targetAccountCurrency.equals(Currency.BGN.getMessage())) {
                amountToDeposit = amountToDeposit * 1.80;
            } else if (targetAccountCurrency.equals(Currency.GBP.getMessage())) {
                amountToDeposit = amountToDeposit * 0.80;
            } else if (targetAccountCurrency.equals(Currency.USD.getMessage())) {
                amountToDeposit = amountToDeposit * 1;
            }
        } else if (sourceAccountCurrency.equals(Currency.GBP.getMessage())) {
            if (targetAccountCurrency.equals(Currency.BGN.getMessage())) {
                amountToDeposit = amountToDeposit * 2.23;
            } else if (targetAccountCurrency.equals(Currency.USD.getMessage())) {
                amountToDeposit = amountToDeposit * 1.24;
            } else if (targetAccountCurrency.equals(Currency.GBP.getMessage())) {
                amountToDeposit = amountToDeposit * 1;
            }
        }
        return amountToDeposit;
    }

}
