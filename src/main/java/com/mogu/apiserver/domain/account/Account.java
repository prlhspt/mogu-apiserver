package com.mogu.apiserver.domain.account;

import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Account", uniqueConstraints = {
        @UniqueConstraint(name = "uc_account_email", columnNames = {"email"})
})
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
