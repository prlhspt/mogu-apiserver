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
import java.util.Optional;

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

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementStage> settlementStages = new ArrayList<>();

    @Builder
    private Settlement(Long totalPrice, String bankCode, String accountNumber, String accountName, String message, SettlementStatus status) {
        this.totalPrice = totalPrice;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.message = message;
        this.status = status;
    }

    public static Settlement create(User user, Long totalPrice, String bankCode, String accountNumber, String accountName, String message, SettlementStatus status) {
        Settlement settlement = new Settlement(totalPrice, bankCode, accountNumber, accountName, message, status);
        settlement.setUser(user);

        return settlement;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addSettlementStage(SettlementStage settlementStage) {
        settlementStages.add(settlementStage);
        settlementStage.setSettlement(this);
    }

    public void updateNotNullValue(String bankCode, String accountNumber, String accountName, String message, Long totalPrice) {
        Optional.ofNullable(bankCode).ifPresent(value -> this.bankCode = value);
        Optional.ofNullable(accountNumber).ifPresent(value -> this.accountNumber = value);
        Optional.ofNullable(accountName).ifPresent(value -> this.accountName = value);
        Optional.ofNullable(message).ifPresent(value -> this.message = value);
        Optional.ofNullable(totalPrice).ifPresent(value -> this.totalPrice = value);
    }

}
