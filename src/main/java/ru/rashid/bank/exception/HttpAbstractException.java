package ru.rashid.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpAbstractException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    HttpAbstractException(String code, String message, HttpStatus status) {
        super(message, null, true, false);
        this.code = code;
        this.status = status;
    }
}
