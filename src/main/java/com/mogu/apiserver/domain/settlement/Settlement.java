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
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetingId;
    private Long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
