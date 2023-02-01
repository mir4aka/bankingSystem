package eu.deltasource.model;

import eu.deltasource.exception.InvalidInputException;

import java.util.HashSet;
import java.util.Set;

public class Owner {

    private String firstName;
    private String lastName;
    private String id;
//    private Set<String> accountTypes;

    public Owner(String firstName, String lastName) {
        try {
            setFirstName(firstName);
            setLastName(lastName);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
//        this.accountTypes = new HashSet<>(2);
    }

//    public void assignAccounts(String type) {
//        this.accountTypes.add(type);
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName.isBlank()) {
            throw new InvalidInputException("First name cannot be blank.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName.isBlank()) {
            throw new InvalidInputException("Last name cannot be blank.");
        }
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Set<String> getAccountTypes() {
//        return accountTypes;
//    }

    @Override
    public String toString() {
//        String types = String.join(", ", this.accountTypes);
        return "FirstName = " + this.firstName + '\n' +
                "LastName = " + this.lastName + '\n' +
                "Id = " + this.id;
    }
}
