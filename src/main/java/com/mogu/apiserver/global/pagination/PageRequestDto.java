package com.mogu.apiserver.global.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageRequestDto {

    private Long page;
    private Long limit;

    public PageQuery toPageQuery() {
        return new PageQuery(page != null ? page : 1, limit != null ? limit : 10);
    }

}
