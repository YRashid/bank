package ru.rashid.bank.exception.handler;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rashid.bank.exception.HttpAbstractException;

@Slf4j
@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<ErrorDescription> transactionExceptionHandler(TransactionSystemException e,
                                                                        HttpServletRequest request) {
        log.error("Handle TransactionSystemException: {}. Request: {}", e, request.getServletPath());
        if (e.getOriginalException() instanceof PessimisticLockingFailureException) {
            return createResponse("Locked", "Ресурс заблокирован", HttpStatus.LOCKED);
        }

        return createdUntypedErrorResponse();
    }

    @ExceptionHandler(HttpAbstractException.class)
    protected ResponseEntity<ErrorDescription> httpAbstractExceptionHandler(HttpAbstractException e,
                                                                            HttpServletRequest request) {
        log.info("Handle HttpAbstractException: {}. Request: {}", e, request.getServletPath());
        return createResponse(e.getCode(), e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDescription> othersExceptionHandler(Exception e, HttpServletRequest request) {
        log.error("Other Exception message: {}. Request: {}", e, request.getServletPath());
        return createdUntypedErrorResponse();
    }

    private ResponseEntity<ErrorDescription> createdUntypedErrorResponse() {
        return createResponse("UntypedError", "Внутренняя ошибка", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDescription> createResponse(String code, String message, HttpStatus status) {
        var errorDescription = new ErrorDescription(code, message);
        return new ResponseEntity<>(errorDescription, status);
    }


}
