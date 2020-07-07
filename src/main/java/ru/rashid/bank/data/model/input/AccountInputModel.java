package ru.rashid.bank.data.model.input;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInputModel {
    @NotNull(message = "Can't be null")
    @Min(value = 1, message = "Should be positive")
    private Long id;

    @NotNull(message = "Can't be null")
    @DecimalMin(value = "0.0", message = "Can't be negative")
    private BigDecimal balance;
}
