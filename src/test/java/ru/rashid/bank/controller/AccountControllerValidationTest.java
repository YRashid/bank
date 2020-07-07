package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.exception.ErrorMessageEnum;
import ru.rashid.bank.exception.handler.ErrorDescription;

import static org.junit.Assert.assertEquals;
import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ALREADY_EXISTS;
import static ru.rashid.bank.exception.ErrorMessageEnum.BALANCE_CANNOT_BE_NEGATIVE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerValidationTest extends AccountControllerTestBase {

    @Test
    public void accountAlreadyExists() throws Exception {
        Long existingId = testAccountHelper.createAccount().getId();
        BigDecimal balance = BigDecimal.ONE;

        var input = new AccountInputModel(existingId, balance);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, ACCOUNT_ALREADY_EXISTS);
    }

    @Test
    public void balanceCannotBeNegative() throws Exception {
        long id = RandomUtils.nextLong(1000L, Long.MAX_VALUE);
        BigDecimal balance = BigDecimal.valueOf(-1);

        var input = new AccountInputModel(id, balance);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, BALANCE_CANNOT_BE_NEGATIVE);
    }


    private void callAndCheckError(AccountInputModel input,
                                   HttpStatus httpStatus,
                                   ErrorMessageEnum errorEnum) throws Exception {

        ErrorDescription errorDescription = callCreateUserNegative(input, httpStatus);
        assertEquals(errorEnum.name(), errorDescription.getCode());
    }
}