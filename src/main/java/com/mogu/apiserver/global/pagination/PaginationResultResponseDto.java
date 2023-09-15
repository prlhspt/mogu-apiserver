package com.mogu.apiserver.global.pagination;

import com.mogu.apiserver.global.util.ApiResponseEntity;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

public class PaginationResultResponseDto<T> {
    private final List<T> data;
    private final Long totalDataCount;
    private final Long maxPage;

    public PaginationResultResponseDto(List<T> data, Long totalDataCount, Long maxPage) {
        this.data = data;
        this.totalDataCount = totalDataCount;
        this.maxPage = maxPage;
    }

    public static <T> PaginationResultResponseDto<T> of(List<T> data, PaginationResult paginationResult) {
        return new PaginationResultResponseDto(data, paginationResult.getTotalResultsCount(), paginationResult.getMaxPagesCount());
    }
}
