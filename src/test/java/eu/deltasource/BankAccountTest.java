package eu.deltasource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    Owner owner = new Owner("ivan", "Ivanov");
    BankInstitution bankInstitution = new BankInstitution("dskk", "asd");
    BankAccount account = new BankAccount(owner, "26", bankInstitution, "20", "BGN", 245, "CurrentAccount");

    @Test
    public void testCurrencyShouldThrowIllegalArgumentException() throws IllegalArgumentException{

    }

}