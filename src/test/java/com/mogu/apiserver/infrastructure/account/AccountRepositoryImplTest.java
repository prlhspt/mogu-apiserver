package com.mogu.apiserver.infrastructure.account;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.infrastructure.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class AccountRepositoryImplTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 계정을 찾는다.")
    void findByEmailWithUser() {
        Account account = Account.builder()
                .email("test@.test.com")
                .password("test1234")
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();


        account.setUser(user);

        userRepository.save(user);
        accountJpaRepository.save(account);

        Account result = accountRepository.findByEmailWithUser(account.getEmail())
                .orElseThrow(AccountNotFoundException::new);

        assertThat(result.getEmail()).isEqualTo(account.getEmail());
    }

}