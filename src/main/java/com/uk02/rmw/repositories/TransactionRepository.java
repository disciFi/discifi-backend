package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_Id(Long accountId);
    List<Transaction> findByCategory_Id(Long categoryId);

    // This query fetches the Transaction and also eagerly loads the
    // related Account and Category entities in a single database trip.
    @Query("SELECT t FROM Transaction t JOIN FETCH t.account JOIN FETCH t.category WHERE t.account.id = :accountId")
    List<Transaction> findByAccountIdWithDetails(Long accountId);
}
