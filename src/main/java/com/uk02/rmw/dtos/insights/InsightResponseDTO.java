package com.uk02.rmw.dtos.insights;

import com.uk02.rmw.enums.InsightPeriod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InsightResponseDTO (
        @NotNull Long id,
        @NotNull String content,
        @NotNull LocalDateTime generatedAt,
        @NotNull InsightPeriod type
) { }
