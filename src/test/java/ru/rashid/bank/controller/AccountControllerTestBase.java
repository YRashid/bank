package ru.rashid.bank.controller;

import org.springframework.http.HttpStatus;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.model.output.AccountOutputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;

abstract class AccountControllerTestBase extends ControllerTestBase<AccountInputModel, AccountOutputModel> {

    protected AccountControllerTestBase() {
        super(AccountOutputModel.class, "account");
    }

    AccountOutputModel callCreateUserPositive(AccountInputModel input) throws Exception {
        return callPositive(input);
    }

    ErrorDescription callCreateUserNegative(AccountInputModel input, HttpStatus status) throws Exception {
        return callNegative(input, status);
    }
}
