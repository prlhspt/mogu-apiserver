package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementLevel {
    @Id
    private Long id;

    @ManyToOne
    private Settlement settlement;
    private Integer level;

    @ManyToOne
    private User user;

    private String name;
    private String phone;

    private Long price;

    @Enumerated(EnumType.STRING)
    private SettlementLevelType type;

    private Boolean settlementComplete;
    private Integer priority;
}
