package ru.rashid.bank.init;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.repository.AccountRepository;

@Component
@RequiredArgsConstructor
public class BootstrapPopulator {
    private final AccountRepository accountRepository;

    @PostConstruct
    public void postConstruct() {
        createAccounts();
    }

    private void createAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (long i = 0; i < 1000; i++) {
            Account account = new Account();
            account.setId(i);
            account.setBalance(BigDecimal.valueOf(1000));
            accounts.add(account);
        }
        accountRepository.saveAll(accounts);
    }
}
