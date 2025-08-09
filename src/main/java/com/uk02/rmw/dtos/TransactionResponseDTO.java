package com.uk02.rmw.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO (
    Long id,
    String title,
    BigDecimal amount,
    String type,
    LocalDate date,
    Long accountId,
    String accountName,
    Long categoryId,
    String categoryName
) { }
