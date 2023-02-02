package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BankInstitutionTest {

    @Test
    void testIfABlankBankNameIsEntered() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            BankInstitution bankInstitution = new BankInstitution("    ", "ivan 2");
        });
    }

    @Test
    void testIfABlankAddressIsEntered() {
        //GIVEN
        Assertions.assertThrows(InvalidInputException.class, () -> {
            BankInstitution bankInstitution = new BankInstitution("dsk", "         ");
        });
    }

}