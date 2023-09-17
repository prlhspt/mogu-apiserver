package com.mogu.apiserver.presentation.settlement;

import com.mogu.apiserver.application.settlement.SettlementService;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettlementController.class)
class SettlementControllerTest {

    @MockBean
    JwtTokenProvider jwtTokenProvider;

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
    void findSettlementGroup() throws Exception {
        mockMvc.perform(get("/settlements")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}