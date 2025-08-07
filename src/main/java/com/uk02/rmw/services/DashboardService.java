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

        return new DashboardDTO(todaySpend, thisWeekSpend, thisMonthSpend);
    }

    private BigDecimal calculateSpendForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findUserExpensesBetweenDates(userId, startDate, endDate);
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
