package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;

import java.util.Optional;

public interface SettlementRepository {

    PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery);

    Optional<Settlement> findSettlementById(Long settlementId);

}
