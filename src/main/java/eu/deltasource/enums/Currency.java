package eu.deltasource.enums;

public enum Currency {
    BGN("BGN"),
    USD("USD"),
    GBP("GBP");

    private final String message;

    Currency(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
