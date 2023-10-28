package com.mogu.apiserver.infrastructure.settlement;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementRepository;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.mogu.apiserver.domain.settlement.QSettlement.settlement;
import static com.mogu.apiserver.domain.settlement.QSettlementStage.settlementStage;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PaginationResult<Settlement> findSettlements(PageDateQuery pageDateQuery, Long userId) {
        
        Long totalCount = jpaQueryFactory.select(settlement.count())
                .from(settlement)
                .where(settlement.user.id.eq(userId),
                        createdDateStart(pageDateQuery.getStartDate()),
                        createdDateEnd(pageDateQuery.getEndDate()))
                .fetchOne();

        List<Settlement> result = jpaQueryFactory.selectFrom(settlement)
                .where(settlement.user.id.eq(userId),
                        createdDateStart(pageDateQuery.getStartDate()),
                        createdDateEnd(pageDateQuery.getEndDate()))
                .join(settlement.settlementStages, settlementStage).fetchJoin()
                .offset(pageDateQuery.getOffset())
                .limit(pageDateQuery.getLimit())
                .fetch();

        return PaginationResult.of(result, pageDateQuery.getLimit(), totalCount, pageDateQuery.hasNext(totalCount));

    }

    @Override
    public Optional<Settlement> findSettlementById(Long settlementId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(settlement)
                        .where(settlement.id.eq(settlementId))
                        .fetchOne()
        );
    }

    private BooleanExpression createdDateStart(OffsetDateTime startDateTime) {
        if (startDateTime == null) {
            return null;
        }
        return settlement.createdDate.goe(startDateTime);
    }


    private BooleanExpression createdDateEnd(OffsetDateTime endDateTime) {
        if (endDateTime == null) {
            return null;
        }
        return settlement.createdDate.lt(endDateTime);
    }
}
