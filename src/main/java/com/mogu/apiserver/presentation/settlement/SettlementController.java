package com.mogu.apiserver.presentation.settlement;

import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.global.pagination.PageDateRequestDto;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.global.pagination.PaginationResultResponseDto;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.settlement.request.CreatedSettlementRequest;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementsResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping("/settlements")
    public String createSettlement(@RequestBody @Valid CreatedSettlementRequest createdSettlementRequest) {
        settlementService.createSettlement(createdSettlementRequest.toServiceRequest());
        return "hello";
    }

    @GetMapping("/settlements")
    public ApiResponseEntity<PaginationResultResponseDto<FindSettlementsResponse>> findSettlements(@ModelAttribute @Valid PageDateRequestDto pageDateRequestDto) {

        PaginationResult<Settlement> settlements = settlementService.findSettlements(pageDateRequestDto.toPageDateQuery());

        List<FindSettlementsResponse> collect = settlements.getData().stream()
                .map(settlement -> FindSettlementsResponse.builder()
                        .totalPrice(settlement.getTotalPrice())
                        .status(settlement.getStatus())
                        .date(settlement.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        PaginationResultResponseDto<FindSettlementsResponse> response = PaginationResultResponseDto.of(collect, settlements);

        return ApiResponseEntity.ok(response);

    }
}
