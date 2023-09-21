package com.mogu.apiserver.infrastructure.settlement;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementRepository;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.domain.settlement.exception.SettlementNotFound;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@Transactional
class SettlementRepositoryImplTest {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private SettlementJpaRepository settlementJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("정산내역을 조회한다.")
    void findSettlements() {
        User user = User.builder()
                .nickname("testNickname")
                .type(UserType.USER)
                .status(UserStatus.ACTIVE)
                .build();

        Settlement settlement = Settlement.builder()
                .totalPrice(3000L)
                .status(SettlementStatus.WAITING)
                .build();

        Settlement settlement2 = Settlement.builder()
                .totalPrice(1500L)
                .status(SettlementStatus.DONE)
                .build();

        PageDateQuery pageDateQuery = PageDateQuery.builder()
                .page(1L)
                .limit(5L)
                .build();

        settlement.setUser(user);
        settlement2.setUser(user);

        userJpaRepository.save(user);
        settlementJpaRepository.save(settlement);
        settlementJpaRepository.save(settlement2);

        PaginationResult<Settlement> paginationSettlements = settlementRepository.findSettlements(pageDateQuery, user.getId());
        List<Settlement> settlements = paginationSettlements.getData();

        assertThat(settlements).extracting(Settlement::getTotalPrice, Settlement::getStatus)
                .containsExactlyInAnyOrder(tuple(3000L, SettlementStatus.WAITING), tuple(1500L, SettlementStatus.DONE));

    }

    @Test
    @DisplayName("정산내역을 id로 조회한다.")
    void findSettlementById() {
        User user = User.builder()
                .nickname("testNickname")
                .type(UserType.USER)
                .status(UserStatus.ACTIVE)
                .build();

        Settlement settlement = Settlement.builder()
                .totalPrice(3000L)
                .status(SettlementStatus.WAITING)
                .build();

        settlement.setUser(user);

        userJpaRepository.save(user);
        settlementJpaRepository.save(settlement);

        Settlement findSettlement = settlementRepository.findSettlementById(settlement.getId(), user.getId())
                .orElseThrow(SettlementNotFound::new);

        assertThat(findSettlement.getTotalPrice()).isEqualTo(3000L);
        assertThat(findSettlement.getStatus()).isEqualTo(SettlementStatus.WAITING);
    }
}