package com.mogu.apiserver.application.settlement;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementParticipant;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import com.mogu.apiserver.domain.settlement.exception.MissingPercentageException;
import com.mogu.apiserver.domain.settlement.exception.SettlementNotFound;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.global.pagination.PageDateQuery;
import com.mogu.apiserver.global.pagination.PaginationResult;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.settlement.SettlementJpaRepository;
import com.mogu.apiserver.infrastructure.user.UserJpaRepository;
import com.mogu.apiserver.presentation.settlement.response.CreateSettlementResponse;
import com.mogu.apiserver.presentation.settlement.response.FindSettlementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SettlementServiceTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private SettlementJpaRepository settlementJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("정산을 생성한다.")
    void createSettlement() {

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        CreateSettlementServiceRequest request = CreateSettlementServiceRequest.builder()
                .bankCode("001")
                .accountName("홍길동")
                .accountNumber("123456789")
                .message("정산 요청합니다.")
                .settlementStage(
                        List.of(
                                CreateSettlementServiceRequest.CreateSettlementStagesServiceRequest.builder()
                                        .level(1)
                                        .totalPrice(10000L)
                                        .participants(
                                                List.of(
                                                        CreateSettlementServiceRequest.CreateSettlementParticipantsServiceRequest.builder()
                                                                .name("홍길동")
                                                                .settlementType(SettlementType.DUTCH_PAY)
                                                                .price(10000L)
                                                                .priority(1)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();

        CreateSettlementResponse settlement = settlementService.createSettlement(request, user.getId());

        assertThat(settlement).isNotNull();
        assertThat(settlement.getSettlementId()).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("정산을 생성할 때, 정산 참여자의 타입이 퍼센트인데 percentage 가 null 이면 예외를 발생시킨다.")
    void createSettlementNoPercentageException() {
        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        CreateSettlementServiceRequest request = CreateSettlementServiceRequest.builder()
                .bankCode("001")
                .accountName("홍길동")
                .accountNumber("123456789")
                .message("정산 요청합니다.")
                .settlementStage(
                        List.of(
                                CreateSettlementServiceRequest.CreateSettlementStagesServiceRequest.builder()
                                        .level(1)
                                        .totalPrice(10000L)
                                        .participants(
                                                List.of(
                                                        CreateSettlementServiceRequest.CreateSettlementParticipantsServiceRequest.builder()
                                                                .name("홍길동")
                                                                .settlementType(SettlementType.PERCENT)
                                                                .price(10000L)
                                                                .priority(1)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .build();

        assertThatThrownBy(() -> settlementService.createSettlement(request, user.getId()))
                .isInstanceOf(MissingPercentageException.class);
    }


    @Test
    @DisplayName("정산을 조회한다.")
    void findSettlements() {

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        Settlement settlement = Settlement.builder()
                .accountName("홍길동")
                .accountNumber("123456789")
                .bankCode("001")
                .message("정산 요청합니다.")
                .status(SettlementStatus.WAITING)
                .build();

        SettlementStage settlementStage = SettlementStage.builder()
                .level(1)
                .totalPrice(10000L)
                .build();

        SettlementParticipant settlementParticipant = SettlementParticipant.builder()
                .name("홍길동")
                .settlementType(SettlementType.DUTCH_PAY)
                .price(10000L)
                .priority(1)
                .settlementParticipantStatus(SettlementParticipantStatus.WAITING)
                .build();

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        settlementStage.addSettlementParticipant(settlementParticipant);
        settlement.addSettlementStage(settlementStage);
        settlement.setUser(user);
        settlementJpaRepository.save(settlement);

        PaginationResult<Settlement> settlements = settlementService.findSettlements(
                PageDateQuery.builder()
                        .page(1L)
                        .limit(10L)
                        .build(),
                user.getId()
        );

        assertThat(settlements).isNotNull();
        assertThat(settlements.getData().size()).isEqualTo(1);
        assertThat(settlements.getTotalResultsCount()).isEqualTo(1);
        assertThat(settlements.getRequestLimit()).isEqualTo(10);
        assertThat(settlements.getHasNext()).isEqualTo(false);
        assertThat(settlements.getData())
                .extracting("bankCode", "accountName", "accountNumber", "message")
                .contains(tuple("001", "홍길동", "123456789", "정산 요청합니다."));
    }

    @Test
    @DisplayName("정산을 id로 조회한다.")
    void findSettlement() {

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        Settlement settlement = Settlement.builder()
                .accountName("홍길동")
                .accountNumber("123456789")
                .bankCode("001")
                .message("정산 요청합니다.")
                .status(SettlementStatus.WAITING)
                .build();

        SettlementStage settlementStage = SettlementStage.builder()
                .level(1)
                .totalPrice(10000L)
                .build();

        SettlementParticipant settlementParticipant = SettlementParticipant.builder()
                .name("홍길동")
                .settlementType(SettlementType.DUTCH_PAY)
                .price(10000L)
                .priority(1)
                .settlementParticipantStatus(SettlementParticipantStatus.WAITING)
                .build();

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        settlementStage.addSettlementParticipant(settlementParticipant);
        settlement.addSettlementStage(settlementStage);
        settlement.setUser(user);
        settlementJpaRepository.save(settlement);

        FindSettlementResponse findSettlementResponse = settlementService.findSettlement(settlement.getId());

        assertThat(findSettlementResponse).isNotNull();
        assertThat(findSettlementResponse)
                .extracting("bankCode", "accountName", "accountNumber", "message", "userId")
                .contains("001", "홍길동", "123456789", "정산 요청합니다.", user.getId());

    }

    @Test
    @DisplayName("정산을 id로 조회할 때, 해당 정산이 없으면 예외를 발생시킨다.")
    void findSettlementNotFoundSettlementException() {

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        assertThatThrownBy(() -> settlementService.findSettlement(1L))
                .isInstanceOf(SettlementNotFound.class)
                .hasMessage("정산 내역을 찾을 수 없습니다.");

    }

}