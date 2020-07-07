package ru.rashid.bank.controller;

import org.springframework.http.HttpStatus;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.model.output.TransferOutputModel;
import ru.rashid.bank.exception.handler.ErrorDescription;

abstract class TransferMoneyControllerTestBase extends ControllerTestBase<TransferInputModel, TransferOutputModel> {
    protected TransferMoneyControllerTestBase() {
        super(TransferOutputModel.class, "transferMoney");
    }

    TransferOutputModel callTransferMoneyPositive(TransferInputModel input) throws Exception {
        return callPositive(input);
    }

    ErrorDescription callTransferMoneyNegative(TransferInputModel input, HttpStatus status) throws Exception {
        return callNegative(input, status);
    }
}
