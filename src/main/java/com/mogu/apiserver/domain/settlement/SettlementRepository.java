package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;

public interface SettlementRepository {

    PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery);

}
