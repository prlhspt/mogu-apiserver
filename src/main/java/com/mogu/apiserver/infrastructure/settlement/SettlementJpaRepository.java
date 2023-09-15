package com.mogu.apiserver.infrastructure.settlement;

import com.mogu.apiserver.domain.settlement.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementJpaRepository extends JpaRepository<Settlement, Long> {
}
