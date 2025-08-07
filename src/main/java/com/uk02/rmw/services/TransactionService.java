package com.uk02.rmw.services;

import com.uk02.rmw.dtos.TransactionDTO;
import com.uk02.rmw.models.Account;
import com.uk02.rmw.models.Category;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.AccountRepository;
import com.uk02.rmw.repositories.CategoryRepository;
import com.uk02.rmw.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Transaction createTransaction(TransactionDTO dto, User user) {
        // check if the account exists among the user's accounts
        Account account = accountRepository.findById(dto.accountId())
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or does not belong to user"));
        // check if the user has access to the category
        Category category = categoryRepository.findById(dto.categoryId())
                .filter(cat -> cat.getUser() == null || cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));

        if ("Expense".equalsIgnoreCase(dto.type())) {
            account.setBalance(account.getBalance().subtract(dto.amount()));
        } else if ("Income".equalsIgnoreCase(dto.type())) {
            account.setBalance(account.getBalance().add(dto.amount()));
        }

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .title(dto.title())
                .amount(dto.amount())
                .type(dto.type())
                .date(dto.date())
                .account(account)
                .category(category)
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Transaction> listTransactionsByAccountId(Long accountId, User user) {
        accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or does not belong to user"));

        return transactionRepository.findByAccountIdWithDetails(accountId);
    }

    public List<Transaction> listTransactionByUser(User user) {
        return transactionRepository.findByUserIdWithDetails(user.getId());
    }

    public List<Transaction> listTransactionsByCategoryId(Long categoryId, User user) {
        categoryRepository.findById(categoryId)
                .filter(cat -> cat.getUser() == null || cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));

        return transactionRepository.findByCategoryIdWithDetails(categoryId);
    }


}
