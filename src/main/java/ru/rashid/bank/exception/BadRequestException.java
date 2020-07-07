package ru.rashid.bank.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpAbstractException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(ErrorMessageEnum errorMessage) {
        super(errorMessage.name(), errorMessage.getText(), HTTP_STATUS);
    }
}
