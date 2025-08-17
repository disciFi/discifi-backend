package com.uk02.rmw.controllers;

import com.uk02.rmw.dtos.budgets.BudgetRequestDTO;
import com.uk02.rmw.dtos.budgets.BudgetResponseDTO;
import com.uk02.rmw.models.Budget;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createOrUpdateBudget(
            @Valid @RequestBody BudgetRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        Budget budget = budgetService.createOrUpdateBudget(dto, user);
        return new ResponseEntity<>(budget, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getBudgets(
            @RequestParam("period") String periodStr,
            @AuthenticationPrincipal User user
    ) {
        YearMonth period = YearMonth.parse(periodStr);
        List<BudgetResponseDTO> budgets = budgetService.getBudgetsForPeriod(period, user);
        return ResponseEntity.ok(budgets);
    }
}