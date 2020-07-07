package ru.rashid.bank.data.model.output;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rashid.bank.data.entity.Account;

@Data
@NoArgsConstructor
public class AccountOutputModel {
    private Long id;
    private BigDecimal balance;

    public AccountOutputModel(Account source) {
        this.id = source.getId();
        this.balance = source.getBalance();
    }
}
