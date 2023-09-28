package com.mogu.apiserver.infrastructure.user;

import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mogu.apiserver.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(user)
                .where(user.id.eq(userId))
                .fetchOne()

        );
    }

}
