package com.uk02.rmw.services;

import com.uk02.rmw.dtos.DashboardDTO;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardDTO getDashboardSummary(User user) {
        LocalDate today = LocalDate.now();

        BigDecimal todaySpend = calculateSpendForPeriod(user.getId(), today, today);
        BigDecimal thisWeekSpend = calculateSpendForPeriod(user.getId(), today.with(DayOfWeek.MONDAY), today.with(DayOfWeek.SUNDAY));
        BigDecimal thisMonthSpend = calculateSpendForPeriod(user.getId(), today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()));

        BigDecimal previousDaySpend = calculateSpendForPeriod(user.getId(), today.minusDays(1), today.minusDays(1));
        BigDecimal previousWeekSpend = calculateSpendForPeriod(user.getId(), today.minusWeeks(1).with(DayOfWeek.MONDAY), today.minusWeeks(1).with(DayOfWeek.SUNDAY));
        BigDecimal previousMonthSpend = calculateSpendForPeriod(user.getId(), today.minusMonths(1).withDayOfMonth(1), today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth()));

        return new DashboardDTO(todaySpend, thisWeekSpend, thisMonthSpend, previousDaySpend, previousWeekSpend, previousMonthSpend);
    }

    private BigDecimal calculateSpendForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findUserExpensesBetweenDates(userId, startDate, endDate);
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
