package com.mogu.apiserver.domain.nonuser;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NonUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    @Enumerated(EnumType.STRING)
    private NonUserStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    private User convertUser;
}
