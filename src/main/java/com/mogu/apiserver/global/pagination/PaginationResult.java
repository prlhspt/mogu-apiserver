package com.mogu.apiserver.global.pagination;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PaginationResult<T> {
    private List<T> data;

    private Long requestLimit;

    private Long totalResultsCount;

    private Boolean hasNext;

    public Long getMaxPagesCount() {
        Double pagesCountDouble = totalResultsCount.doubleValue() / requestLimit;
        Long pagesCountLong = pagesCountDouble.longValue();

        if (pagesCountDouble - pagesCountLong > 0) {
            return pagesCountLong + 1;
        } else {
            return pagesCountLong;
        }
    }

    public static <T> PaginationResult<T> of(List<T> data, Long limit, Long totalDataCount, Boolean hasNext) {
        return new PaginationResult<T>(data, limit, totalDataCount, hasNext);
    }

}