package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SettlementType settlementType;

    @NotNull
    private Long price;

    @NotNull
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SettlementParticipantStatus settlementParticipantStatus;

    private Integer percentage;

    @Builder
    private SettlementParticipant(String name, SettlementType settlementType, Long price, Integer priority, SettlementParticipantStatus settlementParticipantStatus, Integer percentage) {
        this.name = name;
        this.settlementType = settlementType;
        this.price = price;
        this.priority = priority;
        this.settlementParticipantStatus = settlementParticipantStatus;
        this.percentage = percentage;
    }

    public static SettlementParticipant create(String name, SettlementType settlementType, Long price, Integer priority, SettlementParticipantStatus settlementParticipantStatus, Integer percentage) {
        return new SettlementParticipant(name, settlementType, price, priority, settlementParticipantStatus, percentage);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private SettlementStage settlementStage;

    public void setSettlementStage(SettlementStage settlementStage) {
        this.settlementStage = settlementStage;
    }

    public void updateNotNullValue(String name, SettlementType settlementType, Long price, Integer priority, SettlementParticipantStatus settlementParticipantStatus, Integer percentage) {
        Optional.ofNullable(name).ifPresent(value -> this.name = value);
        Optional.ofNullable(settlementType).ifPresent(value -> this.settlementType = value);
        Optional.ofNullable(price).ifPresent(value -> this.price = value);
        Optional.ofNullable(priority).ifPresent(value -> this.priority = value);
        Optional.ofNullable(settlementParticipantStatus).ifPresent(value -> this.settlementParticipantStatus = value);
        Optional.ofNullable(percentage).ifPresent(value -> this.percentage = value);
    }

}
