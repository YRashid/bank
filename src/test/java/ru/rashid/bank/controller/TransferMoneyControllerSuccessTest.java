package ru.rashid.bank.controller;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.input.TransferInputModel;

import static ru.rashid.bank.helper.TestAccountHelper.DEFAULT_BALANCE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferMoneyControllerSuccessTest extends ControllerTestBase {

    @Test
    public void simpleTransfer() {
        Account from = testAccountHelper.createAccount();
        Account to = testAccountHelper.createAccount();
        BigDecimal amount = BigDecimal.ONE;

        var input = new TransferInputModel(from.getId(), to.getId(), amount);
        var response = callTransferMoneyPositive(input);

        checkBalanceHelper.checkResponseBalances(response, amount);
        checkBalanceHelper.checkBalancesInDatabase(from.getId(), to.getId(), amount);
    }

    @Test
    public void circleTransfer() {
        Account account1 = testAccountHelper.createAccount();
        Account account2 = testAccountHelper.createAccount();
        Account account3 = testAccountHelper.createAccount();
        BigDecimal amount = BigDecimal.ONE;

        var input1 = new TransferInputModel(account1.getId(), account2.getId(), amount);
        callTransferMoneyPositive(input1);
        var input2 = new TransferInputModel(account2.getId(), account3.getId(), amount);
        callTransferMoneyPositive(input2);
        var input3 = new TransferInputModel(account3.getId(), account1.getId(), amount);
        callTransferMoneyPositive(input3);

        checkBalanceHelper.checkActualBalance(account1.getId(), DEFAULT_BALANCE);
        checkBalanceHelper.checkActualBalance(account2.getId(), DEFAULT_BALANCE);
        checkBalanceHelper.checkActualBalance(account3.getId(), DEFAULT_BALANCE);
    }

    @Test
    public void zeroBalanceAllowed() {
        Account from = testAccountHelper.createAccount();
        Account to = testAccountHelper.createAccount();
        BigDecimal amount = DEFAULT_BALANCE;

        var input = new TransferInputModel(from.getId(), to.getId(), amount);
        var result = callTransferMoneyPositive(input);
        checkBalanceHelper.checkResponseBalances(result, amount);
    }

}