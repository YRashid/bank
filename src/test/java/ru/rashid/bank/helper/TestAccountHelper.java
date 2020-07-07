package ru.rashid.bank.helper;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.repository.AccountRepository;

@Service
public class TestAccountHelper {
    public static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(100);

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount() {
        Account account = new Account();
        account.setId(RandomUtils.nextLong(1000L, Long.MAX_VALUE));
        account.setBalance(DEFAULT_BALANCE);
        return accountRepository.save(account);
    }
}
