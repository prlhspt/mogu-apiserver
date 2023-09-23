package com.mogu.apiserver.presentation.settlement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.presentation.settlement.request.CreateSettlementRequest;
import com.mogu.apiserver.presentation.settlement.request.CreateSettlementRequest.CreateSettlementParticipantsRequest;
import com.mogu.apiserver.presentation.settlement.request.CreateSettlementRequest.CreateSettlementStagesRequest;
import com.mogu.apiserver.presentation.settlement.request.UpdateSettlementRequest;
import com.mogu.apiserver.presentation.settlement.request.UpdateSettlementRequest.UpdateSettlementParticipantsRequest;
import com.mogu.apiserver.presentation.settlement.request.UpdateSettlementRequest.UpdateSettlementStagesRequest;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettlementController.class)
class SettlementControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    SettlementService settlementService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("정산을 생성한다.")
    void createSettlement() throws Exception {

        CreateSettlementRequest createSettlementRequest = CreateSettlementRequest.builder()
                .bankCode("004")
                .accountName("홍길동")
                .accountNumber("123456789")
                .message("정산 요청합니다.")
                .totalPrice(10000L)
                .userId(1L)
                .settlementStage(List.of(
                        CreateSettlementStagesRequest.builder()
                                .level(1)
                                .participants(List.of(
                                        CreateSettlementParticipantsRequest.builder()
                                                .name("홍길동")
                                                .price(10000L)
                                                .priority(1)
                                                .settlementType(SettlementType.DUTCH_PAY)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        CreateSettlementResponse createSettlementResponse = CreateSettlementResponse.builder()
                .settlementId(1L)
                .build();

        when(settlementService.createSettlement(any(), any(Long.class))).thenReturn(createSettlementResponse);

        mockMvc.perform(post("/settlements/users/1")
                        .content(objectMapper.writeValueAsString(createSettlementRequest))
                        .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.settlementId").exists());

    }

    @Test
    @DisplayName("정산 목록을 조회한다.")
    void findSettlements() throws Exception {

        PaginationResult result = PaginationResult.of(List.of(), 0L, 0L, false);

        when(settlementService.findSettlements(any(PageDateQuery.class), any(Long.class))).thenReturn(result);

        mockMvc.perform(get("/settlements/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.settlements").exists())
                .andExpect(jsonPath("$.data.totalDataCount").exists())
                .andExpect(jsonPath("$.data.maxPage").exists())
                .andExpect(jsonPath("$.data.hasNext").exists());
    }

    @Test
    @DisplayName("정산 내역을 조회한다.")
    void findSettlement() throws Exception {

        when(settlementService.findSettlement(any(Long.class), any(Long.class))).thenReturn(null);
        mockMvc.perform(get("/settlements/1/users/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("정산 내역을 수정한다.")
    void updateSettlement() throws Exception {

        UpdateSettlementRequest updateSettlementRequest = UpdateSettlementRequest.builder()
                .bankCode("004")
                .accountName("홍길동")
                .accountNumber("123456789")
                .message("정산 요청합니다.")
                .totalPrice(10000L)
                .userId(1L)
                .settlementStage(List.of(
                        UpdateSettlementStagesRequest.builder()
                                .level(1)
                                .participants(List.of(
                                        UpdateSettlementParticipantsRequest.builder()
                                                .name("홍길동")
                                                .price(10000L)
                                                .priority(1)
                                                .settlementType(SettlementType.DUTCH_PAY)
                                                .settlementParticipantStatus(SettlementParticipantStatus.DONE)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(settlementService.updateSettlement(any(), any(Long.class), any(Long.class))).thenReturn(null);
        mockMvc.perform(patch("/settlements/1/users/1")
                        .content(objectMapper.writeValueAsString(updateSettlementRequest))
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());

    }

}