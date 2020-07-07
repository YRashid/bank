package ru.rashid.bank.exception;

public enum ErrorMessageEnum {
    INSUFFICIENT_FUNDS("Insufficient funds"),
    ACCOUNT_ALREADY_EXISTS("Account with this id already exists"),
    ACCOUNT_NOT_FOUND("Account with id %s is not found"),
    TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN("Transferring money to yourself is forbidden"),
    ;

    private final String text;

    ErrorMessageEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
