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
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.domain.settlement.exception.MissingPercentageException;
import com.mogu.apiserver.domain.settlement.exception.SettlementNotFound;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.global.property.AwsS3Property;
import com.mogu.apiserver.infrastructure.s3.S3PreSignedUrlGenerator;
import com.mogu.apiserver.infrastructure.settlement.SettlementJpaRepository;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.GeneratePreSignedUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private static final String S3_PREFIX = "settlements";

    private final SettlementRepository settlementRepository;

    private final SettlementJpaRepository settlementJpaRepository;

    private final AccountRepository accountRepository;

    private final S3PreSignedUrlGenerator s3PreSignedUrlGenerator;

    private final AwsS3Property awsS3Property;


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
                SettlementStatus.WAITING,
                createSettlementServiceRequest.getSettlementImages()
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


    public GeneratePreSignedUrlsResponse generateSettlementPreSignedUrl() {
        String preSignedUrl = s3PreSignedUrlGenerator.getPreSignedUrl(
                awsS3Property.bucketName,
                S3_PREFIX,
                UUID.randomUUID() + ".jpg"
        );

        return GeneratePreSignedUrlsResponse.builder()
                .url(preSignedUrl)
                .build();
    }

}
