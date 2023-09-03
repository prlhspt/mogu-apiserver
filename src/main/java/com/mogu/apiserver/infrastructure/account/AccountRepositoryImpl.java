package com.mogu.apiserver.infrastructure.account;

import com.mogu.apiserver.domain.account.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mogu.apiserver.domain.account.QAccount.*;
import static com.mogu.apiserver.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Account> findByEmailWithUser(String email) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(account)
                .join(account.user, user).fetchJoin()
                .where(account.email.eq(email))
                .fetchOne());
    }
}
