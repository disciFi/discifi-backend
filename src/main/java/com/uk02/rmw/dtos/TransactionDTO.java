package com.uk02.rmw.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO (
        @NotEmpty String title,
        @NotNull @Positive BigDecimal amount,
        @NotEmpty String type,
        @NotNull LocalDate date,
        @NotNull Long accountId,
        @NotNull Long categoryId
) {
}
