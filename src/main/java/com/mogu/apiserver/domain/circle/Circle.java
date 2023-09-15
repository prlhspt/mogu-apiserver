package com.mogu.apiserver.domain.circle;

import com.mogu.apiserver.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Circle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String bankName;
    private String accountName;
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Circle circle;

}
