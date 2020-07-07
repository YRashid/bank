package ru.rashid.bank.service.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rashid.bank.data.entity.Account;
import ru.rashid.bank.data.model.input.TransferInputModel;
import ru.rashid.bank.data.repository.AccountRepository;
import ru.rashid.bank.exception.AccountNotFoundException;
import ru.rashid.bank.exception.BadRequestException;

import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ID_MUST_BE_SET;
import static ru.rashid.bank.exception.ErrorMessageEnum.AMOUNT_MUST_BE_POSITIVE;
import static ru.rashid.bank.exception.ErrorMessageEnum.AMOUNT_MUST_BE_SET;
import static ru.rashid.bank.exception.ErrorMessageEnum.INSUFFICIENT_FUNDS;
import static ru.rashid.bank.exception.ErrorMessageEnum.TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN;

@Service
@RequiredArgsConstructor
public class TransferMoneyValidationService {
    private final AccountRepository accountRepository;

    /**
     * Проверить, что
     * сумма перевода задана и она не отрицательна
     * аккаунты существуют и они разные
     */
    public void validateTransferMoney(TransferInputModel input) {
        checkAmount(input.getAmount());
        checkAccountsIds(input);
    }

    /**
     * Проверить, что баланс позволяет провести списание
     */
    public void validateBalance(Account fromAccount, BigDecimal amount) {
        if (amount.compareTo(fromAccount.getBalance()) > 0) {
            throw new BadRequestException(INSUFFICIENT_FUNDS);
        }
    }

    private void checkAccountsIds(TransferInputModel input) {
        Long fromId = input.getFromId();
        Long toId = input.getToId();
        checkAccountIdIsNotNull(fromId);
        checkAccountIdIsNotNull(toId);

        Set<Long> existingIds = accountRepository.getExistingAccountIds(List.of(fromId, toId));
        checkIsAccountExists(fromId, existingIds);
        checkIsAccountExists(toId, existingIds);

        checkIsAccountsDifferent(input);
    }

    private void checkAccountIdIsNotNull(Long accountId) {
        if (accountId == null) {
            throw new BadRequestException(ACCOUNT_ID_MUST_BE_SET);
        }
    }

    private void checkIsAccountsDifferent(TransferInputModel input) {
        if (input.getFromId().equals(input.getToId())) {
            throw new BadRequestException(TRANSFERRING_TO_YOURSELF_IS_FORBIDDEN);
        }
    }

    private void checkIsAccountExists(Long accountId, Set<Long> existingIds) {
        if (!existingIds.contains(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadRequestException(AMOUNT_MUST_BE_SET);
        }

        if (BigDecimal.ZERO.compareTo(amount) > -1) {
            throw new BadRequestException(AMOUNT_MUST_BE_POSITIVE);
        }
    }

}
