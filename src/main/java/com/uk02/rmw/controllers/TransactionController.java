package com.uk02.rmw.controllers;

import com.uk02.rmw.dtos.TransactionDTO;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("create")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO, @AuthenticationPrincipal User user) {
        Transaction createdTransaction = transactionService.createTransaction(transactionDTO, user);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("list")
    public ResponseEntity<Object> listTransactions(@AuthenticationPrincipal User user) {
        List<Transaction> transactions = transactionService.listTransactionByUser(user);
        return ResponseEntity.ok(transactions);
    }
}
