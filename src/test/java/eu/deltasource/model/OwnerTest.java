package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Test
    void testIfABlankFirstNameIsEntered() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            Owner owner = new Owner("          ", "Ivanov");
        });
    }

    @Test
    void testIfABlankLastNameIsEntered() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            Owner owner = new Owner("Ivan", "  ");
        });
    }

    @Test
    void testIfABlankIdIsEntered() {
        //GIVEN
        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");

        Assertions.assertThrows(InvalidInputException.class, () -> {
            Owner owner = new Owner("Ivan", "Ivanov");
            BankAccount bankAccount = new BankAccount(owner, "     ", bankInstitution, "odsjdspf", "GBP", -30, "CurrentAccount");
        });
    }
}