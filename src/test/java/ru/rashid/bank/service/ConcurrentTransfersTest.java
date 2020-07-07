package ru.rashid.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rashid.bank.configuration.BankNoneWebTest;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.helper.TestAccountHelper;
import ru.rashid.bank.helper.TestCheckBalanceHelper;

import static java.util.concurrent.CompletableFuture.runAsync;
import static ru.rashid.bank.helper.TestAccountHelper.DEFAULT_BALANCE;

@RunWith(SpringRunner.class)
@BankNoneWebTest
public class ConcurrentTransfersTest {
    private final static BigDecimal DEFAULT_AMOUNT = BigDecimal.ONE;
    private final static int ONE_DIRECTION_THREADS_COUNT = 101;
    private final static int ALL_THREADS_COUNT = ONE_DIRECTION_THREADS_COUNT * 2;
    private ExecutorService executorService;

    @Autowired
    private TransferMoneyService transferMoneyService;
    @Autowired
    private TestCheckBalanceHelper checkBalanceHelper;
    @Autowired
    private TestAccountHelper testAccountHelper;

    @Before
    public void before() {
        executorService = Executors.newFixedThreadPool(ALL_THREADS_COUNT);
    }

    @After
    public void after() {
        executorService.shutdown();
    }

    @Test
    public void aLotOfTransfersBackAndForth() {
        Long firstId = testAccountHelper.createAccount().getId();
        Long secondId = testAccountHelper.createAccount().getId();

        var subtractForBackward = BigDecimal.valueOf(0.1);

        var forward = new TransferInputModel(firstId, secondId, DEFAULT_AMOUNT);
        var backward = new TransferInputModel(secondId, firstId, DEFAULT_AMOUNT.subtract(subtractForBackward));

        Collection<CompletableFuture<Void>> futures = new ArrayList<>(ALL_THREADS_COUNT);
        var barrier = new CyclicBarrier(ALL_THREADS_COUNT);

        for (int i = 0; i < ONE_DIRECTION_THREADS_COUNT; i++) {
            futures.add(asyncTransfer(forward, barrier));
            futures.add(asyncTransfer(backward, barrier));
        }

        futures.forEach(CompletableFuture::join);

        var expectedDifference = subtractForBackward.multiply(new BigDecimal(ONE_DIRECTION_THREADS_COUNT));
        checkBalanceHelper.checkActualBalance(firstId, DEFAULT_BALANCE.subtract(expectedDifference));
        checkBalanceHelper.checkActualBalance(secondId, DEFAULT_BALANCE.add(expectedDifference));
    }

    private CompletableFuture<Void> asyncTransfer(TransferInputModel input, CyclicBarrier barrier) {
        return runAsync(() -> {
            barrierAwait(barrier);
            transferMoneyService.transfer(input);
        }, executorService);
    }

    private void barrierAwait(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
