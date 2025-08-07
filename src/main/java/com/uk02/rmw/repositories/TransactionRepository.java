package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.category.id = :categoryId")
    List<Transaction> findByCategoryIdWithDetails(Long categoryId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.account.id = :accountId")
    List<Transaction> findByAccountIdWithDetails(Long accountId);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.account.user.id = :userId")
    List<Transaction> findByUserIdWithDetails(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.account.user.id = :userId AND t.type = 'Expense' AND t.date BETWEEN :start AND :end")
    List<Transaction> findUserExpensesBetweenDates(Long userId, LocalDate start, LocalDate end);
}
