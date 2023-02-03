package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void testIfABlankFirstNameIsEnteredShouldThrowAnException() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            BankAccountOwner owner = new BankAccountOwner("          ", "Ivanov", "02");
        });
    }

    @Test
    void testIfABlankLastNameIsEnteredShouldThrowAnException() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            BankAccountOwner owner = new BankAccountOwner("Ivan", "  ", "02");
        });
    }

    @Test
    void testIfABlankIdIsEnteredShouldThrowAnException() {
        //GIVEN
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");

        Assertions.assertThrows(InvalidInputException.class, () -> {
            BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
            BankAccount bankAccount = new BankAccount(owner, bankInstitution, "odsjdspf", "GBP", -30, "CurrentAccount");
        });
    }
}