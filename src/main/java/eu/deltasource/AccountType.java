package eu.deltasource;

public enum AccountType {

    CURRENT_ACCOUNT("CurrentAccount"),
    SAVINGS_ACCOUNT("SavingsAccount");
    private final String message;
    AccountType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
