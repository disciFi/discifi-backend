package com.uk02.rmw.dtos.insights;

import com.uk02.rmw.enums.InsightPeriod;

import java.io.Serializable;

public record InsightRequestDTO(
    Long userId,
    InsightPeriod period
) implements Serializable {}