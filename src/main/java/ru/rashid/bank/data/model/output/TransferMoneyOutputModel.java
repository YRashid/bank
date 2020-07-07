package ru.rashid.bank.data.model.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rashid.bank.data.model.TransferMoneyAccounts;

@Data
@NoArgsConstructor
public class TransferMoneyOutputModel {
    private AccountOutputModel from;
    private AccountOutputModel to;

    public TransferMoneyOutputModel(TransferMoneyAccounts source) {
        this.from = new AccountOutputModel(source.getFrom());
        this.to = new AccountOutputModel(source.getTo());
    }
}
