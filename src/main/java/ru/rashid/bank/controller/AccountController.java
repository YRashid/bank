package ru.rashid.bank.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.model.output.AccountOutputModel;
import ru.rashid.bank.service.AccountService;
import ru.rashid.bank.service.validation.AccountValidationService;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountValidationService validationService;

    @GetMapping("{id}")
    public AccountOutputModel getAccountById(@Valid @Min(value = 1, message = "Should be positive")
                                             @PathVariable("id") Long id) {
        var account = accountService.getAccountById(id);
        return new AccountOutputModel(account);
    }

    @PostMapping
    public AccountOutputModel createAccount(@Valid @RequestBody AccountInputModel input) {
        validationService.validateNewAccountData(input);
        var account = accountService.createAccount(input);
        return new AccountOutputModel(account);
    }

}
