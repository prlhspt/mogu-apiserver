package com.mogu.apiserver.global.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PageDateRequestDto {
    @Schema(description = "페이지 번호", example = "1")
    private Long page;

    @Schema(description = "페이지 크기", example = "10")
    private Long limit;

    @Schema(description = "시작 날짜", example = "2023-01-01T00:00:00+09:00")
    private OffsetDateTime startDate;

    @Schema(description = "종료 날짜", example = "2023-01-01T00:00:00+09:00")
    private OffsetDateTime endDate;

    public PageDateQuery toPageDateQuery() {
        return PageDateQuery.builder()
                .page(page != null ? page : 1)
                .limit(limit != null ? limit : 10)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
