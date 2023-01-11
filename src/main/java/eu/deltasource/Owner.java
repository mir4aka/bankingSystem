package eu.deltasource;

public class Owner {
    private String firstName;
    private String lastName;
    private String id;

//    private Transactions transactions;

    public Owner(String firstName, String lastName, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
//        this.transactions = new Transactions();
    }

    public Owner() {
        this("Ivan", "Ivanov", "2503");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Transactions getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(Transactions transactions) {
//        this.transactions = transactions;
//    }
}
