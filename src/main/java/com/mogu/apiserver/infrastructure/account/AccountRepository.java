package com.mogu.apiserver.infrastructure.account;

import com.mogu.apiserver.domain.account.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByEmailWithUser(String email);
}
