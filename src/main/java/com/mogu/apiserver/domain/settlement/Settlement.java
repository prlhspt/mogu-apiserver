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
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankCode;

    private String accountNumber;
    private String accountName;
    private String message;


    @Enumerated(EnumType.STRING)
    @NotNull(message = "정산 상태는 필수입니다.")
    private SettlementStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "settlement_image", joinColumns = @JoinColumn(name = "settlement_id", referencedColumnName = "id"))
    private Set<SettlementImage> settlementImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "정산 요청자는 필수입니다.")
    private User user;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementStage> settlementStages = new ArrayList<>();

    @Builder
    public Settlement(String bankCode, String accountNumber, String accountName, String message, SettlementStatus status, List<String> settlementImages) {
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.message = message;
        this.status = status;
        this.settlementImages = settlementImages != null
                ? settlementImages.stream()
                .map(SettlementImage::create)
                .collect(toSet())
                : null;
    }

    public static Settlement create(User user, String bankCode, String accountNumber, String accountName, String message, SettlementStatus status, List<String> settlementImages) {
        Settlement settlement = new Settlement(bankCode, accountNumber, accountName, message, status, settlementImages);
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

    public void updateNotNullValue(String bankCode, String accountNumber, String accountName, String message) {
        Optional.ofNullable(bankCode).ifPresent(value -> this.bankCode = value);
        Optional.ofNullable(accountNumber).ifPresent(value -> this.accountNumber = value);
        Optional.ofNullable(accountName).ifPresent(value -> this.accountName = value);
        Optional.ofNullable(message).ifPresent(value -> this.message = value);
    }

}
