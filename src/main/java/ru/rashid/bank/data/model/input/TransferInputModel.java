package ru.rashid.bank.data.model.input;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferInputModel {
    private Long fromId;
    private Long toId;
    private BigDecimal amount;
}
