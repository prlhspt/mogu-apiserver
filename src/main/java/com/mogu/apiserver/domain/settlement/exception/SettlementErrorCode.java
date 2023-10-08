package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.ErrorObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SettlementErrorCode implements ErrorObject {

    SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 내역을 찾을 수 없습니다."),
    SETTLEMENT_STAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 단계를 찾을 수 없습니다."),
    SETTLEMENT_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 참여자를 찾을 수 없습니다."),
    MISSING_PERCENTAGE(HttpStatus.BAD_REQUEST, "정산 참여자의 정산 비율이 없습니다."),
    PERCENTAGE_MISMATCH(HttpStatus.BAD_REQUEST, "정산 참여자의 정산 비율이 정산 종류와 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
