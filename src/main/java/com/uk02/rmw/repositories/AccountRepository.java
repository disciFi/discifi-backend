package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser_IdAndActive(Long userId, Boolean status);
}
