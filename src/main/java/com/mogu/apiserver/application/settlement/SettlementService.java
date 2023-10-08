package com.mogu.apiserver.application.settlement;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementParticipantsServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementStageRequestService;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementParticipant;
import com.mogu.apiserver.domain.settlement.SettlementRepository;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.domain.settlement.exception.*;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.infrastructure.settlement.SettlementJpaRepository;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.UpdateSettlementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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
                .orElseThrow(AccountNotFoundException::new);

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
                            .map(
                                    settlementParticipantRequest -> {
                                        if (settlementParticipantRequest.getSettlementType().equals(SettlementType.PERCENT) && settlementParticipantRequest.getPercentage() == null) {
                                            throw new MissingPercentageException();
                                        }

                                        return SettlementParticipant.create(
                                                settlementParticipantRequest.getName(),
                                                settlementParticipantRequest.getSettlementType(),
                                                settlementParticipantRequest.getPrice(),
                                                settlementParticipantRequest.getPriority(),
                                                SettlementParticipantStatus.WAITING,
                                                settlementParticipantRequest.getPercentage()
                                        );
                                    }
                            )
                            .toList();

                    for (SettlementParticipant participant : participants) {
                        stage.addSettlementParticipant(participant);
                    }

                    return stage;
                })
                .toList();

        for (SettlementStage settlementStage : settlementStages) {
            settlement.addSettlementStage(settlementStage);
        }

        Settlement saveSettlement = settlementJpaRepository.save(settlement);

        return CreateSettlementResponse.builder()
                .settlementId(saveSettlement.getId())
                .build();
    }

    public PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery, Long userId) {
        return settlementRepository.findSettlements(pageDateQuery, userId);
    }

    public FindSettlementResponse findSettlement(Long settlementId) {
        Settlement settlement = settlementRepository.findSettlementById(settlementId)
                .orElseThrow(SettlementNotFound::new);

        return FindSettlementResponse.of(settlement);

    }

    @Transactional
    public UpdateSettlementResponse updateSettlement(UpdateSettlementServiceRequest updateSettlementServiceRequest, Long settlementId, Long userId) {

        Settlement settlement = settlementRepository.findSettlementById(settlementId)
                .orElseThrow(SettlementNotFound::new);

        settlement.updateNotNullValue(updateSettlementServiceRequest.getBankCode(), updateSettlementServiceRequest.getAccountNumber(), updateSettlementServiceRequest.getAccountName(), updateSettlementServiceRequest.getMessage(), updateSettlementServiceRequest.getTotalPrice());

        Map<Long, SettlementStage> settlementStageMap = settlement.getSettlementStages().stream()
                .collect(toMap(SettlementStage::getId, Function.identity()));

        updateSettlementServiceRequest.getSettlementStage().forEach(updateSettlementStageRequestService -> {
            SettlementStage settlementStage = Optional.ofNullable(settlementStageMap.get(updateSettlementStageRequestService.getId()))
                    .orElseThrow(SettlementStageNotFound::new);
            settlementStage.updateNotNullValue(updateSettlementStageRequestService.getLevel());

            Map<Long, SettlementParticipant> participantMap = settlementStage.getSettlementParticipants().stream()
                    .collect(toMap(SettlementParticipant::getId, Function.identity()));

            updateSettlementStageRequestService.getParticipants().forEach(updateSettlementParticipantsServiceRequest -> {
                if (updateSettlementParticipantsServiceRequest.getSettlementType() == SettlementType.PERCENT && updateSettlementParticipantsServiceRequest.getPercentage() == null) {
                    throw new MissingPercentageException();
                }

                if (updateSettlementParticipantsServiceRequest.getSettlementType() != SettlementType.PERCENT && updateSettlementParticipantsServiceRequest.getPercentage() != null) {
                    throw new PercentageMismatchException();
                }

                SettlementParticipant settlementParticipant = Optional.ofNullable(participantMap.get(updateSettlementParticipantsServiceRequest.getId()))
                        .orElseThrow(SettlementParticipantNotFound::new);
                settlementParticipant.updateNotNullValue(updateSettlementParticipantsServiceRequest.getName(), updateSettlementParticipantsServiceRequest.getSettlementType(), updateSettlementParticipantsServiceRequest.getPrice(), updateSettlementParticipantsServiceRequest.getPriority(), updateSettlementParticipantsServiceRequest.getSettlementParticipantStatus(), updateSettlementParticipantsServiceRequest.getPercentage());
            });
        });

        return UpdateSettlementResponse.builder()
                .settlementId(settlement.getId())
                .build();
    }

}
