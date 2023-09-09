package com.mogu.apiserver.domain.user;

import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("사용자가 활성화되어있는지 확인한다.")
    void isActivated() {
        User user = User.builder()
                .nickname("test")
                .type(UserType.USER)
                .status(UserStatus.ACTIVE)
                .build();

        boolean activated = user.isActivated();

        assertThat(activated).isTrue();
    }

    @Test
    @DisplayName("사용자가 비활성화되어있는지 확인한다.")
    void isDeactivated() {
        User user = User.builder()
                .nickname("test")
                .type(UserType.USER)
                .status(UserStatus.INACTIVE)
                .build();

        boolean activated = user.isActivated();

        assertThat(activated).isFalse();
    }
}