package ru.rashid.bank.helper;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.output.AccountOutputModel;
import ru.rashid.bank.data.model.output.TransferMoneyOutputModel;
import ru.rashid.bank.data.repository.AccountRepository;

import static org.junit.Assert.assertEquals;

@Service
public class TestCheckBalanceHelper {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void checkBalancesInDatabase(Long fromId, Long toId, BigDecimal differenceFromDefault) {
        checkActualBalance(fromId, TestAccountHelper.DEFAULT_BALANCE.subtract(differenceFromDefault));
        checkActualBalance(toId, TestAccountHelper.DEFAULT_BALANCE.add(differenceFromDefault));
    }

    public void checkResponseBalances(TransferMoneyOutputModel result, BigDecimal differenceFromDefault) {
        AccountOutputModel from = result.getFrom();
        AccountOutputModel to = result.getTo();
        checkResponseBalance(from, TestAccountHelper.DEFAULT_BALANCE.subtract(differenceFromDefault));
        checkResponseBalance(to, TestAccountHelper.DEFAULT_BALANCE.add(differenceFromDefault));
    }

    /**
     * Проверить баланс в ответе сервиса
     */
    public void checkResponseBalance(AccountOutputModel account, BigDecimal expectedBalance) {
        checkBalance(account.getBalance(), expectedBalance);
    }

    /**
     * Проверить баланс в БД
     */
    @Transactional
    public void checkActualBalance(Long accountId, BigDecimal expectedBalance) {
        Account actualAccount = accountRepository.getOne(accountId);
        checkBalance(actualAccount, expectedBalance);
    }

    private void checkBalance(Account account, BigDecimal expectedBalance) {
        checkBalance(account.getBalance(), expectedBalance);
    }

    private void checkBalance(BigDecimal actualBalance, BigDecimal expectedBalance) {
        assertEquals(expectedBalance.doubleValue(), actualBalance.doubleValue(), 0.0000001);
    }
}
