package com.mogu.apiserver.presentation.settlement;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettlementController.class)
class SettlementControllerTest {

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


}