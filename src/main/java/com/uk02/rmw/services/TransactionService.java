package com.uk02.rmw.services;

import com.uk02.rmw.dtos.TransactionDTO;
import com.uk02.rmw.dtos.TransactionResponseDTO;
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

    @Transactional
    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionDTO dto, User user) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .filter(t -> t.getAccount().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));

        Account account = existingTransaction.getAccount();

        // remove the old transaction from the account
        if ("Expense".equalsIgnoreCase(existingTransaction.getType())) {
            account.setBalance(account.getBalance().add(existingTransaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(existingTransaction.getAmount()));
        }

        // update the account if the account has changed
        Account currentAccount = account;
        if (!account.getId().equals(dto.accountId())) {
            currentAccount = accountRepository.findById(dto.accountId())
                    .filter(acc -> acc.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Account not found or does not belong to user"));
            accountRepository.save(account);
        }

        // update the transaction
        if ("Expense".equalsIgnoreCase(dto.type())) {
            currentAccount.setBalance(currentAccount.getBalance().subtract(dto.amount()));
        } else {
            currentAccount.setBalance(currentAccount.getBalance().add(dto.amount()));
        }

        Category currentCategory = categoryRepository.findById(dto.categoryId())
                .filter(cat -> cat.getUser() == null || cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));

        existingTransaction.setTitle(dto.title());
        existingTransaction.setAmount(dto.amount());
        existingTransaction.setType(dto.type());
        existingTransaction.setDate(dto.date());
        existingTransaction.setAccount(currentAccount);
        existingTransaction.setCategory(currentCategory);

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);

        return new TransactionResponseDTO(
                updatedTransaction.getId(),
                updatedTransaction.getTitle(),
                updatedTransaction.getAmount(),
                updatedTransaction.getType(),
                updatedTransaction.getDate(),
                updatedTransaction.getAccount().getId(),
                updatedTransaction.getAccount().getName(),
                updatedTransaction.getCategory().getId(),
                updatedTransaction.getCategory().getName()
        );
    }

    @Transactional
    public void deleteTransaction(Long transactionId, User user) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .filter(t -> t.getAccount().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));

        Account account = transaction.getAccount();
        if ("Expense".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

    public List<Transaction> listTransactionByUser(User user) {
        return transactionRepository.findByUserIdWithDetails(user.getId());
    }

    public List<Transaction> listTransactionsByAccountId(Long accountId, User user) {
        accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or does not belong to user"));

        return transactionRepository.findByAccountIdWithDetails(accountId);
    }

    public List<Transaction> listTransactionsByCategoryId(Long categoryId, User user) {
        categoryRepository.findById(categoryId)
                .filter(cat -> cat.getUser() == null || cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));
        List<Long> accountIds = accountRepository.findByUser_Id(user.getId())
                .stream()
                .map(Account::getId)
                .toList();

        return transactionRepository.findByCategoryIdWithDetails(categoryId, accountIds);
    }

}
