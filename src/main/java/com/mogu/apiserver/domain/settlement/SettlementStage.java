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

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Settlement settlement;

    @OneToMany(mappedBy = "settlementStage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettlementParticipant> settlementParticipants = new ArrayList<>();

    @Builder
    private SettlementStage(Integer level) {
        this.level = level;
    }

    public static SettlementStage create(Integer level) {
        SettlementStage settlementStage = new SettlementStage(level);

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
