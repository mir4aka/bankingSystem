package eu.deltasource.enums;

public enum ExceptionMessage {
    FIRST_NAME_INVALID("First name cannot be blank."),
    LAST_NAME_INVALID("Last name cannot be blank."),
    ID_INVALID("Id cannot be blank."),
    INVALID_CURRENCY("Invalid currency for a bank account. You may select BGN, USD or GBP."),
    INVALID_AMOUNT("You can't assign a negative value of the balance of the account."),
    NOT_ENOUGH_MONEY("Not enough money for a withdrawal."),
    INVALID_ACCOUNT_TYPE("Account type can be either `Current` or `Savings` type of account."),
    INVALID_BANK_NAME("Bank name cannot be blank."),
    INVALID_ADDRESS("Bank address cannot be blank.");

    final String message;
    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
