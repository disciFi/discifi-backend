package com.uk02.rmw.services;

import com.uk02.rmw.dtos.transactions.BalanceAdjustmentDTO;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public Account createAccount(Account account, User user) {
        account.setUser(user);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(User user) {
        return accountRepository.findByUser_IdAndActive(user.getId(), true);
    }

    public Account updateAccount(Long accountId, Account accountDetails, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()) && acc.getActive())
                .orElseThrow(() -> new RuntimeException("Account not found or inactive or access denied"));

        account.setName(accountDetails.getName());
        account.setType(accountDetails.getType());

        return accountRepository.save(account);
    }

    public void deleteAccount(Long accountId, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or inactive or access denied"));

        account.setActive(false);
        accountRepository.save(account);
    }

    public Account restoreAccount(Long accountId, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()) && !acc.getActive())
                .orElseThrow(() -> new RuntimeException("Account not found or already active or access denied"));

        account.setActive(true);
        return accountRepository.save(account);
    }

    @Transactional
    public void applyBalanceAdjustment(Long accountId, BalanceAdjustmentDTO dto, User user) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()) && acc.getActive())
                .orElseThrow(() -> new RuntimeException("Account not found or inactive or access denied"));

        account.setBalance(account.getBalance().add(dto.amount()));
        accountRepository.save(account);
        Category adjustmentCategory = categoryRepository.findByName("Balance Adjustment");

        Transaction adjustmentTransaction = Transaction.builder()
                .title("Adjustment: " + dto.reason())
                .amount(dto.amount().abs())
                .type(dto.amount().compareTo(BigDecimal.ZERO) >= 0 ? "Income" : "Expense")
                .date(LocalDate.now())
                .account(account)
                .category(adjustmentCategory)
                .isRecurring(false)
                .recurrencePeriod(null)
                .build();

        transactionRepository.save(adjustmentTransaction);
    }
}
