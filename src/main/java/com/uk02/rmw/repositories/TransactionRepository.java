package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // fetch the transactions that are of particular category from all the accounts that belong to the current user
    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.category.id = :categoryId AND t.account.user.id = :userId AND t.account.active = true")
    List<Transaction> findByCategoryIdWithDetails(Long categoryId, Long userId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.account.id = :accountId AND t.account.active = true")
    List<Transaction> findByAccountIdWithDetails(Long accountId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.account.user.id = :userId AND t.account.active = true")
    List<Transaction> findByUserIdWithDetails(Long userId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.account.user.id = :userId AND t.type = 'Expense' AND t.date BETWEEN :start AND :end AND t.account.active = true")
    List<Transaction> findUserExpensesBetweenDates(Long userId, LocalDate start, LocalDate end);

    List<Transaction> findByIsRecurringTrueAndNextRecurrenceDateLessThanEqual(LocalDate date);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.user.id = :userId AND t.account.active = true AND t.category.id = :categoryId AND t.type = 'Expense' AND FUNCTION('TO_CHAR', t.date, 'YYYY-MM') = :period")
    BigDecimal sumExpensesByCategoryIdAndPeriod(Long userId, Long categoryId, String period);

}
