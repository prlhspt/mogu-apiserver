package com.mogu.apiserver.infrastructure.account;

import com.mogu.apiserver.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    boolean existsByEmailAllIgnoreCase(String email);

}
