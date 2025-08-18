package com.uk02.rmw.dtos;

import java.math.BigDecimal;

public record DashboardDTO (
        BigDecimal todaySpend,
        BigDecimal thisWeekSpend,
        BigDecimal thisMonthSpend,
        BigDecimal previousDaySpend,
        BigDecimal previousWeekSpend,
        BigDecimal previousMonthSpend
) { }
