package com.mogu.apiserver.domain.user;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_nickname", columnNames = {"nickname"})
})
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserType type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus status;

    @Builder
    public User(String nickname, UserType type, UserStatus status) {
        this.nickname = nickname;
        this.type = type;
        this.status = status;
    }

    public boolean isActivated() {
        return this.status == UserStatus.ACTIVE;
    }

    public void delete() {
        this.status = UserStatus.INACTIVE;
    }


    public void updateNotNullValue(String nickname) {
        Optional.ofNullable(nickname).ifPresent(value -> this.nickname = value);
    }

}
