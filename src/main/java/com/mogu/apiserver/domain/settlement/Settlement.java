package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long totalPrice;

    private String bankCode;

    private String accountNumber;
    private String accountName;
    private String message;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SettlementStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @Builder
    private Settlement(Long totalPrice, String bankCode, String accountNumber, String accountName, String message, SettlementStatus status) {
        this.totalPrice = totalPrice;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.message = message;
        this.status = status;
    }

    public static Settlement create(Long totalPrice, String bankCode, String accountNumber, String accountName, String message, SettlementStatus status) {
        return new Settlement(totalPrice, bankCode, accountNumber, accountName, message, status);
    }

    public void setUser(User user) {
        this.user = user;
    }

}
