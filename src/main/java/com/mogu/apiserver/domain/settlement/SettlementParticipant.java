package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @NotNull
    private Boolean settlementComplete;

    @Builder
    public SettlementParticipant(String name, SettlementType settlementType, Long price, Integer priority, Boolean settlementComplete) {
        this.name = name;
        this.settlementType = settlementType;
        this.price = price;
        this.priority = priority;
        this.settlementComplete = settlementComplete;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private SettlementStage settlementStage;

}
