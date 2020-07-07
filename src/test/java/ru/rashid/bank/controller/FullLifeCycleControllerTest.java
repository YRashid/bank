package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.model.output.AccountOutputModel;

import static org.junit.Assert.assertEquals;
import static ru.rashid.bank.helper.TestAccountHelper.DEFAULT_BALANCE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FullLifeCycleControllerTest extends ControllerTestBase {

    public static final BigDecimal AMOUNT = BigDecimal.ONE;

    @Test
    public void smokeTest() {
        Long idFrom = createAccount();
        Long idTo = createAccount();

        transferMoney(idFrom, idTo);

        AccountOutputModel accountFrom = getAccount(idFrom);
        checkBalanceHelper.checkResponseBalance(accountFrom, DEFAULT_BALANCE.subtract(AMOUNT));

        AccountOutputModel accountTo = getAccount(idTo);
        checkBalanceHelper.checkResponseBalance(accountTo, DEFAULT_BALANCE.add(AMOUNT));
    }

    private AccountOutputModel getAccount(Long id) {
        AccountOutputModel accountOutputModel = callGetAccount(id);
        assertEquals(id, accountOutputModel.getId());
        return accountOutputModel;
    }


    private void transferMoney(Long fromId, Long toId) {
        var input = new TransferInputModel(fromId, toId, AMOUNT);
        callTransferMoneyPositive(input);
    }

    private Long createAccount() {
        long id = testAccountHelper.getRandomId();
        var input = new AccountInputModel(id, DEFAULT_BALANCE);
        callCreateUserPositive(input);
        return id;
    }
}
