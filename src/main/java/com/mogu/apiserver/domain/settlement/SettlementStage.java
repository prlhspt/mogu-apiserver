package com.mogu.apiserver.domain.settlement;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    private SettlementStage(Integer level) {
        this.level = level;
    }

    public static SettlementStage create(Integer level) {
        return new SettlementStage(level);
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

}
