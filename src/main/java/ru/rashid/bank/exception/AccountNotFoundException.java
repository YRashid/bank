package ru.rashid.bank.exception;

import org.springframework.http.HttpStatus;

import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_NOT_FOUND;

public class AccountNotFoundException extends HttpAbstractException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public AccountNotFoundException(Long id) {
        super(ACCOUNT_NOT_FOUND.name(), String.format(ACCOUNT_NOT_FOUND.getText(), id), HTTP_STATUS);
    }
}