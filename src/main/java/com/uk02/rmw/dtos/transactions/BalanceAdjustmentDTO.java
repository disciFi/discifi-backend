package com.uk02.rmw.dtos.transactions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BalanceAdjustmentDTO (
    @NotNull BigDecimal amount,
    @NotEmpty String reason
) { }
