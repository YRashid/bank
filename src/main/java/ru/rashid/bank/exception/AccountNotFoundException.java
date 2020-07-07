package ru.rashid.bank.exception;

import org.springframework.http.HttpStatus;

import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_NOT_FOUND;

public class AccountNotFoundException extends HttpAbstractException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final ErrorMessageEnum ERROR_MESSAGE_ENUM = ACCOUNT_NOT_FOUND;

    public AccountNotFoundException(Long id) {
        super(ERROR_MESSAGE_ENUM.name(), String.format(ERROR_MESSAGE_ENUM.getText(), id), HTTP_STATUS);
    }
}