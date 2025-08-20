package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b JOIN FETCH b.category WHERE b.user.id = :userId AND b.period = :period")
    List<Budget> findByUser_IdAndPeriod(Long userId, YearMonth period);

    Optional<Budget> findByUser_IdAndCategoryIdAndPeriod(Long userId, Long categoryId, YearMonth period);
}