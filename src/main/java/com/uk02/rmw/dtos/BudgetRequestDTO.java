package com.uk02.rmw.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetRequestDTO(
        @NotNull YearMonth period,
        @NotNull @Positive BigDecimal amount,
        @NotNull Long categoryId
) {}