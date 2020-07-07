package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.model.input.AccountInputModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerSuccessTest extends AccountControllerTestBase {

    @Test
    public void createAccount() throws Exception {
        long id = RandomUtils.nextLong(1000L, Long.MAX_VALUE);
        BigDecimal balance = BigDecimal.ONE;

        var input = new AccountInputModel(id, balance);
        var response = callCreateUserPositive(input);

        checkBalanceHelper.checkResponseBalance(response, balance);
        checkBalanceHelper.checkActualBalance(id, balance);
    }
}