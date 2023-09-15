package com.mogu.apiserver.global.pagination;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class PageDateRequestDto {
    private Long page;
    private Long limit;

    private OffsetDateTime startDate;
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
