package ru.rashid.bank.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.repository.AccountRepository;
import ru.rashid.bank.exception.AccountNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(AccountInputModel input) {
        //todo: catch unique constraint violation
        accountRepository.insert(input.getId(), input.getBalance());
        Account account = accountRepository.getOne(input.getId());
        log.info("New user {} with balance {} created", input.getId(), input.getBalance());
        return account;
    }

    public Account getAccountById(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        return accountOptional.orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
