package ru.rashid.bank.exception;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

import static ru.rashid.bank.exception.ErrorMessageEnum.INSUFFICIENT_FUNDS;

public class InsufficientFundsException extends HttpAbstractException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final ErrorMessageEnum ERROR_MESSAGE_ENUM = INSUFFICIENT_FUNDS;

    public InsufficientFundsException(BigDecimal balance) {
        super(ERROR_MESSAGE_ENUM.name(), String.format(ERROR_MESSAGE_ENUM.getText(), balance), HTTP_STATUS);
    }
}
