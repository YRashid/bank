package ru.rashid.bank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.TransferMoneyAccounts;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.repository.AccountRepository;
import ru.rashid.bank.service.validation.TransferMoneyValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferMoneyService {
    private final AccountRepository accountRepository;
    private final TransferMoneyValidationService validationService;

    /**
     * Перевести деньги с одного счета на другой.
     * Метод блокирует записи в таблице account.
     * Блокировка происходит в порядке возрастания id аккаунта.
     *
     * @param input входная информация о переводе.
     * @return результирующие остатки на обоих счетах.
     */
    @Transactional
    public TransferMoneyAccounts transfer(TransferInputModel input) {
        var transferAccounts = getTransferAccountsWithLock(input);
        updateAccounts(transferAccounts, input.getAmount());
        logUpdates(transferAccounts.getFrom(), transferAccounts.getTo(), input.getAmount());
        return transferAccounts;
    }

    private void logUpdates(Account from, Account to, BigDecimal amount) {
        log.info("Successful transfer {} money from id {} to id {}. New balances is {} and {} respectively",
                amount, from.getId(), to.getId(), from.getBalance(), to.getBalance());
    }

    private void updateAccounts(TransferMoneyAccounts transferAccounts, BigDecimal amount) {
        Account fromAccount = transferAccounts.getFrom();
        Account toAccount = transferAccounts.getTo();
        validationService.validateBalance(fromAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.saveAll(List.of(toAccount, fromAccount));
    }

    private TransferMoneyAccounts getTransferAccountsWithLock(TransferInputModel input) {
        Long fromId = input.getFromId();
        Long toId = input.getToId();
        Map<Long, Account> idToAccount;

        if (fromId > toId) {
            idToAccount = getAccountsMapWithLock(toId, fromId);
        } else {
            idToAccount = getAccountsMapWithLock(fromId, toId);
        }

        return new TransferMoneyAccounts(idToAccount.get(fromId), idToAccount.get(toId));
    }

    private Map<Long, Account> getAccountsMapWithLock(Long firstLockId, Long secondLockId) {
        Account first = accountRepository.getForUpdate(firstLockId);
        Account second = accountRepository.getForUpdate(secondLockId);
        return Map.of(first.getId(), first, second.getId(), second);
    }

}
