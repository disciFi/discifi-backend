package com.uk02.rmw.controllers;

import com.uk02.rmw.models.Account;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.AccountService;
import com.uk02.rmw.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account, @AuthenticationPrincipal User user) {
        Account createdAccount = accountService.createAccount(account, user);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAccounts(@AuthenticationPrincipal User user) {
        List<Account> accounts = accountService.getAccountsForUser(user);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("{account_id}/transactions")
    public ResponseEntity<Object> getTransactionsByAccountId(
        @PathVariable Long account_id,
        @AuthenticationPrincipal User user
    ) {
        List<Transaction> transactions = transactionService.listTransactionsByAccountId(account_id, user);
        return ResponseEntity.ok(transactions);
    }
}
