package ru.rashid.bank.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.model.output.TransferMoneyOutputModel;
import ru.rashid.bank.service.TransferMoneyService;
import ru.rashid.bank.service.validation.TransferMoneyValidationService;

@RestController
@RequestMapping("transferMoney")
@AllArgsConstructor
public class TransferMoneyController {
    private final TransferMoneyService transferMoneyService;
    private final TransferMoneyValidationService validationService;

    @PostMapping
    public TransferMoneyOutputModel transferMoney(@RequestBody TransferInputModel input) {
        validationService.validateTransferMoney(input);
        var transferMoneyAccounts = transferMoneyService.transfer(input);
        return new TransferMoneyOutputModel(transferMoneyAccounts);
    }

}
