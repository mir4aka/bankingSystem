package eu.deltasource;

import java.util.HashSet;
import java.util.Set;

public class Owner {
    private String firstName;
    private String lastName;
    private String id;
    private Set<String> accountTypes;

    public Owner(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountTypes = new HashSet<>(2);
    }

    public Owner() {

    }

    public void assignAccounts(String type) {
        this.accountTypes.add(type);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getAccountTypes() {
        return accountTypes;
    }

    @Override
    public String toString() {
        String types = String.join(", ", this.accountTypes);
        return "Owner: " + "\n" +
                "FirstName = " + this.firstName + '\n' +
                "LastName = " + this.lastName + '\n' +
                "Id = " + this.id + '\n' +
                "AccountTypes = " + types + "\n";
    }
}
