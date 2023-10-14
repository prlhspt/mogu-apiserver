package com.mogu.apiserver.infrastructure.user;

import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.UserRepository;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void findById() {
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        userJpaRepository.save(user);

        User result = userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);

        assertThat(result).extracting("nickname", "status", "type")
                .containsExactly("test", UserStatus.ACTIVE, UserType.USER);
    }
}