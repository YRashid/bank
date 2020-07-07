package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;

import static org.junit.Assert.assertEquals;
import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ALREADY_EXISTS;
import static ru.rashid.bank.exception.handler.HttpExceptionHandler.INPUT_PARAMETER_VALIDATION_ERROR;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerValidationTest extends ControllerTestBase {

    @Test
    public void accountAlreadyExists() {
        Long existingId = testAccountHelper.createAccount().getId();
        BigDecimal balance = BigDecimal.ONE;

        var input = new AccountInputModel(existingId, balance);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, ACCOUNT_ALREADY_EXISTS.name());
    }

    @Test
    public void balanceCannotBeNegative() {
        long id = testAccountHelper.getRandomId();
        BigDecimal balance = BigDecimal.valueOf(-1);

        var input = new AccountInputModel(id, balance);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, INPUT_PARAMETER_VALIDATION_ERROR);
    }


    private void callAndCheckError(AccountInputModel input,
                                   HttpStatus httpStatus,
                                   String errorCode) {

        ErrorDescription errorDescription = callCreateUserNegative(input, httpStatus);
        assertEquals(errorCode, errorDescription.getCode());
    }
}