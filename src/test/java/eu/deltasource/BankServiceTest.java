package eu.deltasource;

import eu.deltasource.exception.AccountTypeCannotBeDifferentFromCurrentAndSavingsException;
import eu.deltasource.exception.NotEnoughMoneyToWithdrawException;
import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;
import eu.deltasource.model.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    @Test
    void testIfTheAvailableAmountInTheAccountIncreases() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);


        //WHEN
        bankService.depositMoneyToAccount(bankAccount, 5, dateTime);

        //THEN
        Assertions.assertEquals(35, bankAccount.getAvailableAmount());
    }

    @Test
    void testIfTheAvailableAmountInTheAccountDecreases() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.withdrawMoneyFromAccount(bankAccount, 5, dateTime);

        //THEN
        Assertions.assertEquals(25, bankAccount.getAvailableAmount());
    }

    @Test
    void testToWithdrawMoneyIfAccountBalanceIsNotEnough() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //THEN
        Assertions.assertThrows(NotEnoughMoneyToWithdrawException.class, () -> {
            bankService.withdrawMoneyFromAccount(bankAccount, 31, dateTime);
        });
    }

    @Test
    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromGBPtoUSD() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);

        //THEN
        Assertions.assertEquals(7.99, bankAccount.getAvailableAmount());
        Assertions.assertEquals(42.4, bankAccount2.getAvailableAmount());
    }

    @Test
    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromBGNtoUSD() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "BGN", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);

        //THEN
        Assertions.assertEquals(19.38, bankAccount.getAvailableAmount());
        Assertions.assertEquals(35.5, bankAccount2.getAvailableAmount());
    }

    @Test
    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromBGNtoBGN() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "BGN", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "BGN", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);

        //THEN
        Assertions.assertEquals(17.95, bankAccount.getAvailableAmount());
        Assertions.assertEquals(40, bankAccount2.getAvailableAmount());
    }

    @Test
    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromUSDtoGBP() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankService bankService = new BankService();
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "USD", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "GBP", 30, "CurrentAccount");
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);

        //THEN
        Assertions.assertEquals(15.25, bankAccount.getAvailableAmount());
        Assertions.assertEquals(38, bankAccount2.getAvailableAmount());
    }

    @Test
    void testIfTheBalanceOfTheAccountIsAsSetInTheObject() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");

        //THEN
        Assertions.assertEquals(30, bankAccount.getAvailableAmount());
        Assertions.assertEquals(30, bankAccount2.getAvailableAmount());
    }

    @Test
    void testIfTheTransactionsAreBeingStoredCorrectly() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 300, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "210", bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
        BankService bankService = new BankService();
        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);

        //WHEN
        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);

        //THEN
        Assertions.assertEquals(1, bankAccount.getAccountTransactions().size());
        Assertions.assertEquals(1, bankAccount2.getAccountTransactions().size());
    }
}