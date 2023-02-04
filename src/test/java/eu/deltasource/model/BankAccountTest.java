package eu.deltasource.model;

import eu.deltasource.BankService;
import eu.deltasource.exception.AccountTypeCannotBeDifferentFromCurrentAndSavingsException;
import eu.deltasource.exception.AvailableAmountCannotBeNegativeException;
import eu.deltasource.exception.InvalidCurrencyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class BankAccountTest {

    @Test
    void testIfAccountTypeIsDifferentFromCurrentAndSavingsShouldThrowAnException() {
        //GIVEN
        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");

        Assertions.assertThrows(AccountTypeCannotBeDifferentFromCurrentAndSavingsException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner, "GBP", 30, "gdfgd");
        });
    }

    @Test
    void testIfAccountIsAddedToNumberOfCustomersOfTheBankCorrectly() {
        //GIVEN
        BankService bankService = new BankService();
        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "021");

        BankInstitution dsk = new BankInstitution("dsk", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "GBP", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "USD", 30, "CurrentAccount");

        bankService.addBank(dsk);

        bankService.addBankAccountToBank(bankAccount, dsk);
        bankService.addBankAccountToBank(bankAccount2, dsk);

        //THEN
        Assertions.assertEquals(2, dsk.getNumberOfCustomers().size());
    }

    @Test
    void testIfInvalidCurrencyEnteredShouldAnException() {
        //GIVEN
        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");

        Assertions.assertThrows(InvalidCurrencyException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner,  "ASZ", 30, "CurrentAccount");
        });
    }

    @Test
    void testIfANegativeBalanceIsEnteredShouldThrowAnException() {
        //GIVEN
        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");

        Assertions.assertThrows(AvailableAmountCannotBeNegativeException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner, "GBP", -30, "CurrentAccount");
        });
    }
}