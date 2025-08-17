package com.uk02.rmw.dtos.budgets;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetResponseDTO(
        Long id,
        YearMonth period,
        String categoryName,
        BigDecimal budgetAmount,
        BigDecimal amountSpent,
        BigDecimal amountRemaining
) {}