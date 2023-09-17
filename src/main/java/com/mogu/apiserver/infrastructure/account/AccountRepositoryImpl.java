package com.mogu.apiserver.infrastructure.account;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mogu.apiserver.domain.account.QAccount.account;
import static com.mogu.apiserver.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Account> findByEmailWithUser(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(account)
                        .join(account.user, user).fetchJoin()
                        .where(account.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Account> findByEmailAndActiveUser(String email) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(account)
                        .join(account.user, user)
                        .where(account.email.eq(email), user.status.eq(UserStatus.ACTIVE))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Account> findByIdWithUserFetch(Long userId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(account)
                        .join(account.user, user).fetchJoin()
                        .where(user.id.eq(userId), user.status.eq(UserStatus.ACTIVE))
                        .fetchOne()
        );
    }

    @Override
    public Optional<String> findByIdAndActiveUser(Long userId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(account.email)
                        .from(account)
                        .join(account.user, user)
                        .where(user.id.eq(userId), user.status.eq(UserStatus.ACTIVE))
                        .fetchOne()
        );
    }

}
