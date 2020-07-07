package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.model.input.AccountInputModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerSuccessTest extends ControllerTestBase {

    @Test
    public void createAccount() {
        long id = testAccountHelper.getRandomId();
        BigDecimal balance = BigDecimal.ONE;

        var input = new AccountInputModel(id, balance);
        var response = callCreateUserPositive(input);

        checkBalanceHelper.checkResponseBalance(response, balance);
        checkBalanceHelper.checkActualBalance(id, balance);
    }
}