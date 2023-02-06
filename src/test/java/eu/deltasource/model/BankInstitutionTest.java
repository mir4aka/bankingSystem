package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BankInstitutionTest {

    @Test
    void testIfABlankBankNameIsEnteredShouldThrowAnException() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            //THEN
            BankInstitution bankInstitution = new BankInstitution("    ", "ivan 2");
        });
    }

    @Test
    void testIfABlankAddressIsEnteredShouldThrowAnException() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            //THEN
            BankInstitution bankInstitution = new BankInstitution("dsk", "         ");
        });
    }

}