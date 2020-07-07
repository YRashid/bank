package ru.rashid.bank.exception;

public enum ErrorMessageEnum {
    INSUFFICIENT_FUNDS("Недостаточно средств"),
    BALANCE_CANNOT_BE_NEGATIVE("Баланс не может быть отрицательным"),
    AMOUNT_MUST_BE_POSITIVE("Сумма перевода должна быть положительной"),
    BALANCE_MUST_BE_SET("Баланс должен быть задан"),
    AMOUNT_MUST_BE_SET("Сумма должна быть задана"),
    ACCOUNT_ID_MUST_BE_SET("Идентификатор аккаунта должен быть задан"),
    ACCOUNT_ALREADY_EXISTS("Аккаунт с таким id уже существует"),
    ACCOUNT_NOT_FOUND("Аккаунт с id %s не найден"),
    TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN("Перевод самому себе запрещен"),
    ;

    private final String text;

    ErrorMessageEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
