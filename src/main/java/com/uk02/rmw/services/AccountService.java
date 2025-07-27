package com.uk02.rmw.services;

import com.uk02.rmw.models.Account;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Account account, User user) {
        account.setUser(user);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(User user) {
        return accountRepository.findByUser_Id(user.getId());
    }
}
