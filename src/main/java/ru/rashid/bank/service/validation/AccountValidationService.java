package ru.rashid.bank.service.validation;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.repository.AccountRepository;
import ru.rashid.bank.exception.BadRequestException;

import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ALREADY_EXISTS;
import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ID_MUST_BE_SET;
import static ru.rashid.bank.exception.ErrorMessageEnum.BALANCE_CANNOT_BE_NEGATIVE;
import static ru.rashid.bank.exception.ErrorMessageEnum.BALANCE_MUST_BE_SET;

@Service
@RequiredArgsConstructor
public class AccountValidationService {
    private final AccountRepository accountRepository;

    /**
     * Проверить, что
     * баланс задан и он неотрицательный
     * аккаунт с таким id еще не существует
     */
    public void validateNewAccountData(AccountInputModel input) {
        checkBalance(input.getBalance());
        checkNewAccountId(input.getId());
    }

    private void checkNewAccountId(Long accountId) {
        if (accountId == null) {
            throw new BadRequestException(ACCOUNT_ID_MUST_BE_SET);
        }

        boolean alreadyExists = accountRepository.existsById(accountId);
        if (alreadyExists) {
            throw new BadRequestException(ACCOUNT_ALREADY_EXISTS);
        }
    }

    private void checkBalance(BigDecimal balance) {
        if (balance == null) {
            throw new BadRequestException(BALANCE_MUST_BE_SET);
        }

        if (BigDecimal.ZERO.compareTo(balance) > 0) {
            throw new BadRequestException(BALANCE_CANNOT_BE_NEGATIVE);
        }
    }

}
