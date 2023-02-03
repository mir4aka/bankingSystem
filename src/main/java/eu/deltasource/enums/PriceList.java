package eu.deltasource.enums;

public enum PriceList {
    TAX_TO_SAME_BANK("Tax to same bank"),
    TAX_TO_DIFFERENT_BANK("Tax to different bank"),
    EXCHANGE_TO_DIFFERENT_CURRENCY("Exchange to different currency"),
    EXCHANGE_TO_SAME_CURRENCY("Exchange to same currency");

    final String message;
    PriceList(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
