package com.mogu.apiserver.domain.settlement;

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
public class SettlementStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer level;

    @NotNull(message = "총 금액은 필수입니다.")
    private Long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Settlement settlement;

    @OneToMany(mappedBy = "settlementStage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementParticipant> settlementParticipants = new ArrayList<>();

    @Builder
    private SettlementStage(Integer level, Long totalPrice) {
        this.level = level;
        this.totalPrice = totalPrice;
    }

    public static SettlementStage create(Integer level, Long totalPrice) {
        SettlementStage settlementStage = new SettlementStage(level, totalPrice);

        return settlementStage;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public void addSettlementParticipant(SettlementParticipant settlementParticipant) {
        settlementParticipants.add(settlementParticipant);
        settlementParticipant.setSettlementStage(this);
    }

    public void updateNotNullValue(Integer level) {
        Optional.ofNullable(level).ifPresent(value -> this.level = value);
    }
}
