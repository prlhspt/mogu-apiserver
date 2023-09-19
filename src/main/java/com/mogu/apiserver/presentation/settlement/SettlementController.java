package com.mogu.apiserver.presentation.settlement;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.global.pagination.PageDateRequestDto;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.settlement.request.CreateSettlementRequest;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    private final AuthenticationService authenticationService;

    @PostMapping("/settlements/users/{userId}")
    public ApiResponseEntity<CreateSettlementResponse> createSettlement(
            @PathVariable Long userId,
            @RequestBody @Valid CreateSettlementRequest createSettlementRequest
    ) {

        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(settlementService.createSettlement(createSettlementRequest.toServiceRequest(), userId));
    }

    @GetMapping("/settlements/users/{userId}")
    public ApiResponseEntity<FindSettlementsResponse> findSettlements(
            @PathVariable Long userId,
            @ModelAttribute @Valid PageDateRequestDto pageDateRequestDto
    ) {

        authenticationService.verifyIdentity(userId);

        PaginationResult<Settlement> settlements = settlementService.findSettlements(pageDateRequestDto.toPageDateQuery());
        FindSettlementsResponse findSettlementsResponse = FindSettlementsResponse.of(settlements);

        return ApiResponseEntity.ok(findSettlementsResponse);

    }

    @GetMapping("/settlements/{settlementId}/users/{userId}")
    public ApiResponseEntity<FindSettlementResponse> findSettlement(
            @PathVariable Long settlementId,
            @PathVariable Long userId
    ) {

        authenticationService.verifyIdentity(userId);
        return ApiResponseEntity.ok(settlementService.findSettlement(settlementId, userId));

    }
}
