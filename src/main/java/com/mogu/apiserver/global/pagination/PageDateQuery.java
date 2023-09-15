package com.mogu.apiserver.global.pagination;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class PageDateQuery {

    private final Long page;
    private final Long limit;
    private final Long offset;

    private final OffsetDateTime startDate;
    private final OffsetDateTime endDate;

    @Builder
    public PageDateQuery(Long page, Long limit, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.page = page;
        this.limit = limit;
        this.startDate = startDate;
        this.endDate = endDate;

        this.offset = (page - 1) * limit;
    }

    public Boolean hasNext(Long totalResultCount) {
        return totalResultCount > nextOffset();
    }

    private Long nextOffset() {
        return offset + limit;
    }
}
