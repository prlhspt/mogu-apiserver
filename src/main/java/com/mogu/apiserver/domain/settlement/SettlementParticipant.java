package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.BaseEntity;
import com.mogu.apiserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SettlementType settlementType;

    private Long price;

    private Integer priority;

    private Boolean settlementComplete;

    @ManyToOne
    private User user;

}
