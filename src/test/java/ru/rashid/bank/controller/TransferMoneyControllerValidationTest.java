package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;

import static org.junit.Assert.assertEquals;
import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_NOT_FOUND;
import static ru.rashid.bank.exception.ErrorMessageEnum.INSUFFICIENT_FUNDS;
import static ru.rashid.bank.exception.ErrorMessageEnum.TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN;
import static ru.rashid.bank.exception.handler.HttpExceptionHandler.INPUT_PARAMETER_VALIDATION_ERROR;
import static ru.rashid.bank.helper.TestAccountHelper.DEFAULT_BALANCE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferMoneyControllerValidationTest extends ControllerTestBase {

    @Test
    public void insufficientFunds() {
        Account from = testAccountHelper.createAccount();
        Account to = testAccountHelper.createAccount();

        var input = new TransferInputModel(from.getId(), to.getId(), DEFAULT_BALANCE);
        callTransferMoneyPositive(input);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, INSUFFICIENT_FUNDS.name());
    }

    @Test
    public void accountIdNotFound() {
        Account from = testAccountHelper.createAccount();
        long nonexistentId = testAccountHelper.getRandomId();

        var input = new TransferInputModel(from.getId(), nonexistentId, BigDecimal.ONE);
        callAndCheckError(input, HttpStatus.NOT_FOUND, ACCOUNT_NOT_FOUND.name());
    }

    @Test
    public void accountIdCannotBeNull() {
        Account from = testAccountHelper.createAccount();

        var input = new TransferInputModel(from.getId(), null, BigDecimal.ONE);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, INPUT_PARAMETER_VALIDATION_ERROR);
    }

    @Test
    public void transferringToYourselfIsForbidden() {
        Account from = testAccountHelper.createAccount();

        var input = new TransferInputModel(from.getId(), from.getId(), BigDecimal.ONE);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN.name());
    }

    @Test
    public void amountMustBePositive() {
        Account from = testAccountHelper.createAccount();
        Account to = testAccountHelper.createAccount();

        var input = new TransferInputModel(from.getId(), to.getId(), BigDecimal.valueOf(-1));
        callAndCheckError(input, HttpStatus.BAD_REQUEST, INPUT_PARAMETER_VALIDATION_ERROR);
    }

    @Test
    public void zeroAmountIsForbidden() {
        Account from = testAccountHelper.createAccount();
        Account to = testAccountHelper.createAccount();

        var input = new TransferInputModel(from.getId(), to.getId(), BigDecimal.ZERO);
        callAndCheckError(input, HttpStatus.BAD_REQUEST, INPUT_PARAMETER_VALIDATION_ERROR);
    }

    private void callAndCheckError(TransferInputModel input,
                                   HttpStatus httpStatus,
                                   String errorCode) {

        ErrorDescription errorDescription = callTransferMoneyNegative(input, httpStatus);
        assertEquals(errorCode, errorDescription.getCode());
    }
}
