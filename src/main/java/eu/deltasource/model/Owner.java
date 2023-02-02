package eu.deltasource.model;

import eu.deltasource.enums.ExceptionMessage;
import eu.deltasource.exception.InvalidInputException;

public class Owner {

    private String firstName;
    private String lastName;
    private String id;

    public Owner(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
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
        this.id = id;
    }

    @Override
    public String toString() {
        return "FirstName = " + this.firstName + '\n' +
                "LastName = " + this.lastName + '\n' +
                "Id = " + this.id;
    }
}
