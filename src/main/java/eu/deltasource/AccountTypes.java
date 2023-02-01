package eu.deltasource;

public enum AccountTypes {

    CURRENT_ACCOUNT("CurrentAccount"),
    SAVINGS_ACCOUNT("SavingsAccount");
    private String message;
    AccountTypes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
