package ru.rashid.bank.data.model;

import lombok.Data;
import ru.rashid.bank.data.entity.Account;

/**
 * Внутренняя модель для хранения аккаунтов участвующих в переводе
 */
@Data
public class TransferMoneyAccounts {
    private final Account from;
    private final Account to;
}
