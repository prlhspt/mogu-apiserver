package com.mogu.apiserver.domain.account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByEmailWithUser(String email);

    Optional<Account> findByEmailAndActiveUser(String email);

    Optional<Account> findByIdWithUserFetch(Long userId);

    Optional<String> findByIdAndActiveUser(Long userId);

}
