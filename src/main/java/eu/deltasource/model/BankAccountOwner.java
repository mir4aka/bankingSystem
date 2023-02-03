package eu.deltasource.model;

import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.exception.InvalidInputException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BankAccountOwner {

    private String firstName;
    private String lastName;
    private String id;

    public BankAccountOwner(String firstName, String lastName, String id) {
        setFirstName(firstName);
        setLastName(lastName);
        setId(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName.isBlank()) {
            throw new InvalidInputException(ExceptionMessage.FIRST_NAME_INVALID.getMessage());
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName.isBlank()) {
            throw new InvalidInputException(ExceptionMessage.LAST_NAME_INVALID.getMessage());
        }
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id.isBlank()) {
            throw new InvalidInputException(ExceptionMessage.ID_INVALID.getMessage());
        }

        if (BankInstitution.getBankAccountOwners().contains(id)) {
            throw new InvalidInputException("An account in this bank already exists with this id: " + id);
        }

        this.id = id;
        BankInstitution.addBankAccountToBank(id);
    }

    @Override
    public String toString() {
        return "FirstName = " + this.firstName + '\n' +
                "LastName = " + this.lastName + '\n' +
                "Id = " + this.id;
    }
}
