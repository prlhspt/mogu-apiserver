package com.mogu.apiserver.infrastructure.user;

import com.mogu.apiserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
