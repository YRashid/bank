package ru.rashid.bank.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rashid.bank.data.model.input.AccountInputModel;
import ru.rashid.bank.data.repository.AccountRepository;
import ru.rashid.bank.exception.BadRequestException;

import static ru.rashid.bank.exception.ErrorMessageEnum.ACCOUNT_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AccountValidationService {
    private final AccountRepository accountRepository;

    /**
     * Проверить, что аккаунт с таким id еще не существует
     */
    public void validateNewAccountData(AccountInputModel input) {
        checkThatAccountIsNotExists(input.getId());
    }

    private void checkThatAccountIsNotExists(Long accountId) {
        boolean alreadyExists = accountRepository.existsById(accountId);
        if (alreadyExists) {
            throw new BadRequestException(ACCOUNT_ALREADY_EXISTS);
        }
    }

}
