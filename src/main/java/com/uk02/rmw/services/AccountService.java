package com.uk02.rmw.services;

import com.uk02.rmw.models.Account;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.AccountRepository;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account createAccount(Account account, User user) {
        account.setUser(user);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(User user) {
        return accountRepository.findByUser_Id(user.getId());
    }

    public Account updateAccount(Long accountId, Account accountDetails, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        account.setName(accountDetails.getName());
        account.setType(accountDetails.getType());

        return accountRepository.save(account);
    }

    public void deleteAccount(Long accountId, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        if (!transactionRepository.findByAccountIdWithDetails(accountId).isEmpty()) {
            throw new IllegalStateException("Cannot delete account with existing transactions.");
        }

        accountRepository.delete(account);
    }
}
