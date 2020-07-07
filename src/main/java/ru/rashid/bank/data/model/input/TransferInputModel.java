package ru.rashid.bank.data.model.input;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferInputModel {
    @NotNull
    @Min(value = 1, message = "Should be positive")
    private Long fromId;

    @NotNull
    @Min(value = 1, message = "Should be positive")
    private Long toId;

    @NotNull
    @DecimalMin(value = "0.0", message = "Should be positive", inclusive = false)
    private BigDecimal amount;
}
