package com.mogu.apiserver.application.settlement;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementParticipant;
import com.mogu.apiserver.domain.settlement.SettlementRepository;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.domain.settlement.exception.SettlementNotFound;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.infrastructure.settlement.SettlementJpaRepository;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    private final SettlementJpaRepository settlementJpaRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public CreateSettlementResponse createSettlement(CreateSettlementServiceRequest createSettlementServiceRequest, Long userId) {

        Account account = accountRepository.findByIdWithUserFetch(userId)
                .orElseThrow(() -> new AccountNotFoundException());

        Settlement settlement = Settlement.create(
                account.getUser(),
                createSettlementServiceRequest.getTotalPrice(),
                createSettlementServiceRequest.getBankCode(),
                createSettlementServiceRequest.getAccountNumber(),
                createSettlementServiceRequest.getAccountName(),
                createSettlementServiceRequest.getMessage(),
                SettlementStatus.WAITING
        );

        List<SettlementStage> settlementStages = createSettlementServiceRequest.getSettlementStage().stream()
                .map(settlementStageRequest -> {
                    SettlementStage stage = SettlementStage.create(settlementStageRequest.getLevel());

                    List<SettlementParticipant> participants = settlementStageRequest.getParticipants().stream()
                            .map(settlementParticipant -> SettlementParticipant.create(
                                    settlementParticipant.getName(),
                                    settlementParticipant.getSettlementType(),
                                    settlementParticipant.getPrice(),
                                    settlementParticipant.getPriority(),
                                    SettlementParticipantStatus.WAITING)
                            )
                            .collect(toList());

                    for (SettlementParticipant participant : participants) {
                        stage.addSettlementParticipant(participant);
                    }

                    return stage;
                }).collect(toList());

        for (SettlementStage settlementStage : settlementStages) {
            settlement.addSettlementStage(settlementStage);
        }

        Settlement saveSettlement = settlementJpaRepository.save(settlement);

        return CreateSettlementResponse.builder()
                .settlementId(saveSettlement.getId())
                .build();
    }

    public PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery) {
        return settlementRepository.findSettlements(pageDateQuery);
    }

    public FindSettlementResponse findSettlement(Long settlementId, Long userId) {
        Settlement settlement = settlementRepository.findSettlementById(settlementId)
                .orElseThrow(() -> new SettlementNotFound());

        return FindSettlementResponse.of(settlement);

    }

}
