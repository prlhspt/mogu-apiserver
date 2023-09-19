package com.mogu.apiserver.presentation.settlement.response;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.global.pagination.PaginationResult;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Builder
public class FindSettlementsResponse {

    private final List<SettlementResponse> settlements;

    private final Long totalDataCount;
    private final Long maxPage;
    private final Boolean hasNext;

    @Getter
    @Builder
    private static class SettlementResponse {
        private Long totalPrice;
        private SettlementStatus status;
        private OffsetDateTime date;

    }


    public static FindSettlementsResponse of(PaginationResult<Settlement> paginationSettlements) {
        return FindSettlementsResponse.builder()
                .settlements(paginationSettlements.getData().stream()
                        .map(settlement -> SettlementResponse.builder()
                                .totalPrice(settlement.getTotalPrice())
                                .status(settlement.getStatus())
                                .date(settlement.getCreatedDate())
                                .build())
                        .collect(toList()))
                .totalDataCount(paginationSettlements.getTotalResultsCount())
                .maxPage(paginationSettlements.getMaxPagesCount())
                .hasNext(paginationSettlements.getHasNext())
                .build();

    }
}
