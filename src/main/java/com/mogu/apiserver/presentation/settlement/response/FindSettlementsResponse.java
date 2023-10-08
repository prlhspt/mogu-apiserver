package com.mogu.apiserver.presentation.settlement.response;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.global.pagination.PaginationResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Builder
public class FindSettlementsResponse {

    @Schema(description = "정산 리스트")
    private final List<SettlementResponse> settlements;

    @Schema(description = "전체 데이터 수")
    private final Long totalDataCount;

    @Schema(description = "최대 페이지 수")
    private final Long maxPage;

    @Schema(description = "다음 페이지 존재 여부")
    private final Boolean hasNext;

    @Getter
    @Builder
    private static class SettlementResponse {

        @Schema(description = "정산 ID", example = "1")
        private Long id;

        @Schema(description = "총 금액", example = "10000")
        private Long totalPrice;

        @Schema(description = "정산 상태", example = "WAITING")
        private SettlementStatus status;

        @Schema(description = "정산 생성 날짜", example = "2021-08-01T00:00:00.000Z")
        private OffsetDateTime date;

    }


    public static FindSettlementsResponse of(PaginationResult<Settlement> paginationSettlements) {
        return FindSettlementsResponse.builder()
                .settlements(paginationSettlements.getData().stream()
                        .map(settlement -> SettlementResponse.builder()
                                .id(settlement.getId())
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
