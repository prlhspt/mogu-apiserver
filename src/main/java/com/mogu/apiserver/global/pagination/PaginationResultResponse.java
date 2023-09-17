package com.mogu.apiserver.global.pagination;

import lombok.Getter;

@Getter
public class PaginationResultResponse {
    private final Long totalDataCount;
    private final Long maxPage;
    private final Boolean hasNext;

    public PaginationResultResponse(Long totalDataCount, Long maxPage, Boolean hasNext) {
        this.totalDataCount = totalDataCount;
        this.maxPage = maxPage;
        this.hasNext = hasNext;
    }

    public static PaginationResultResponse of(PaginationResult paginationResult) {
        return new PaginationResultResponse(paginationResult.getTotalResultsCount(), paginationResult.getMaxPagesCount(), paginationResult.getHasNext());
    }
}
