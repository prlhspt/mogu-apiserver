package com.mogu.apiserver.application.settlement;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementRepository;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.infrastructure.settlement.SettlementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    private final SettlementJpaRepository settlementJpaRepository;

    public PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery) {
        return settlementRepository.findSettlements(pageDateQuery);
    }

    public void createSettlement(CreateSettlementServiceRequest createSettlementServiceRequest) {

        Settlement settlement = Settlement.create(
                createSettlementServiceRequest.getTotalPrice(),
                createSettlementServiceRequest.getBankCode(),
                createSettlementServiceRequest.getAccountNumber(),
                createSettlementServiceRequest.getAccountName(),
                createSettlementServiceRequest.getMessage(),
                SettlementStatus.WAITING
        );


        List<SettlementStage> collect = createSettlementServiceRequest.getSettlementStage().stream()
                .map(settlementStage -> {
                            SettlementStage stage = SettlementStage.create(settlementStage.getLevel());
                            stage.setSettlement(settlement);
                            return stage;
                        }
                .collect(Collectors.toList());

//        SettlementStage.builder()
//                        .level()
//                                .build();

//        SettlementParticipant.builder()
//                        .name(createSettlementServiceRequest.getSettlementStage())

        settlementJpaRepository.save(settlement);

    }

}
