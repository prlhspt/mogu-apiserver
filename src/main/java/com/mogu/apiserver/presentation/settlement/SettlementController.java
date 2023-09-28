package com.mogu.apiserver.presentation.settlement;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.global.pagination.PageDateRequestDto;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.settlement.request.CreateSettlementRequest;
import com.mogu.apiserver.presentation.settlement.request.UpdateSettlementRequest;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementsResponse;
import com.mogu.apiserver.presentation.settlement.response.UpdateSettlementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    private final AuthenticationService authenticationService;

    @Operation(summary = "정산 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 생성 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PostMapping("/settlements/users/{userId}")
    public ApiResponseEntity<CreateSettlementResponse> createSettlement(
            @PathVariable Long userId,
            @RequestBody @Valid CreateSettlementRequest createSettlementRequest
    ) {

        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(settlementService.createSettlement(createSettlementRequest.toServiceRequest(), userId));
    }

    @Operation(summary = "정산 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @GetMapping("/settlements/users/{userId}")
    public ApiResponseEntity<FindSettlementsResponse> findSettlements(
            @PathVariable Long userId,
            @ModelAttribute @Valid PageDateRequestDto pageDateRequestDto
    ) {

        authenticationService.verifyIdentity(userId);

        PaginationResult<Settlement> settlements = settlementService.findSettlements(pageDateRequestDto.toPageDateQuery(), userId);
        FindSettlementsResponse findSettlementsResponse = FindSettlementsResponse.of(settlements);

        return ApiResponseEntity.ok(findSettlementsResponse);

    }

    @Operation(summary = "정산 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 상세 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @GetMapping("/settlements/{settlementId}/users/{userId}")
    public ApiResponseEntity<FindSettlementResponse> findSettlement(
            @PathVariable Long settlementId,
            @PathVariable Long userId
    ) {

        authenticationService.verifyIdentity(userId);
        return ApiResponseEntity.ok(settlementService.findSettlement(settlementId, userId));

    }

    @Operation(summary = "정산 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PatchMapping("/settlements/{settlementId}/users/{userId}")
    public ApiResponseEntity<UpdateSettlementResponse> updateSettlement(
            @PathVariable Long settlementId,
            @PathVariable Long userId,
            @RequestBody UpdateSettlementRequest updateSettlementRequest
    ) {

        authenticationService.verifyIdentity(userId);
        return ApiResponseEntity.ok(settlementService.updateSettlement(updateSettlementRequest.toServiceRequest(), settlementId, userId));

    }
}
