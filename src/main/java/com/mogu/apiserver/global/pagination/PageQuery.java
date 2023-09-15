package com.mogu.apiserver.global.pagination;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageQuery {

    private final Long page;
    private final Long limit;

    private final Long offset;

    @Builder
    public PageQuery(Long page, Long limit) {
        this.page = page;
        this.limit = limit;

        this.offset = (page - 1) * limit;
    }
}
