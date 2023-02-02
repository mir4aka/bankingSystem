package eu.deltasource.model;

import eu.deltasource.exception.AccountTypeCannotBeDifferentFromCurrentAndSavingsException;
import eu.deltasource.exception.AvailableAmountCannotBeNegativeException;
import eu.deltasource.exception.InvalidCurrencyException;
import eu.deltasource.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class BankAccountTest {

    @Test
    void testIfAccountTypeIsDifferentFromCurrentAndSavings() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");

        Assertions.assertThrows(AccountTypeCannotBeDifferentFromCurrentAndSavingsException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "gdfgd");
        });
    }

    @Test
    void testIfAccountIsAddedToNumberOfCustomersOfTheBankCorrectly() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        Owner owner2 = new Owner("Gergi", "petrov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
        BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, "201", bankInstitution, "odsjdspf1", "USD", 30, "CurrentAccount");

        //THEN
        Assertions.assertEquals(2, bankInstitution.getNumberOfCustomers().size());
    }

    @Test
    void testIfInvalidCurrencyEnteredThrowsAnException() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");

        Assertions.assertThrows(InvalidCurrencyException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "ASZ", 30, "CurrentAccount");
        });
    }

    @Test
    void testIfANegativeBalanceIsEnteredThrowsAnException() {
        //GIVEN
        Owner owner = new Owner("Ivan", "Ivanov");
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");

        Assertions.assertThrows(AvailableAmountCannotBeNegativeException.class, () -> {
            BankAccount bankAccount = new BankAccount(owner, "20", bankInstitution, "odsjdspf", "GBP", -30, "CurrentAccount");
        });
    }
}