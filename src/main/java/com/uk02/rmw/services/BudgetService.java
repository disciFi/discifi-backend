package com.uk02.rmw.services;

import com.uk02.rmw.dtos.BudgetRequestDTO;
import com.uk02.rmw.dtos.BudgetResponseDTO;
import com.uk02.rmw.models.Budget;
import com.uk02.rmw.models.Category;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.BudgetRepository;
import com.uk02.rmw.repositories.CategoryRepository;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Budget createOrUpdateBudget(BudgetRequestDTO dto, User user) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = budgetRepository
                .findByUser_IdAndCategoryIdAndPeriod(user.getId(), dto.categoryId(), dto.period())
                .orElse(new Budget());

        budget.setUser(user);
        budget.setCategory(category);
        budget.setPeriod(dto.period());
        budget.setAmount(dto.amount());

        return budgetRepository.save(budget);
    }

    public List<BudgetResponseDTO> getBudgetsForPeriod(YearMonth period, User user) {
        List<Budget> budgets = budgetRepository.findByUser_IdAndPeriod(user.getId(), period);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return budgets.stream().map(budget -> {
            BigDecimal amountSpent = transactionRepository.sumExpensesByCategoryIdAndPeriod(
                    user.getId(),
                    budget.getCategory().getId(),
                    period.format(formatter)
            );
            BigDecimal amountRemaining = budget.getAmount().subtract(amountSpent);

            return new BudgetResponseDTO(
                    budget.getId(),
                    budget.getPeriod(),
                    budget.getCategory().getName(),
                    budget.getAmount(),
                    amountSpent,
                    amountRemaining
            );
        }).collect(Collectors.toList());
    }
}