package eu.deltasource;

import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;
import eu.deltasource.model.Owner;
import org.junit.jupiter.api.Test;

class BankAccountTest {

    Owner owner = new Owner("ivan", "Ivanov");
    BankInstitution bankInstitution = new BankInstitution("dskk", "asd");
    BankAccount account = new BankAccount(owner, "26", bankInstitution, "20", "BGN", 245, "CurrentAccount");

    @Test
    public void testCurrencyShouldThrowIllegalArgumentException() throws IllegalArgumentException{

    }

}