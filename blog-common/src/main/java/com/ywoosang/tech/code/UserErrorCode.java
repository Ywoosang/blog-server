package com.ywoosang.tech.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserErrorCode {
    USER_INPUT_ERROR(HttpStatus.BAD_REQUEST, "USER-001", "입력한 사용자 정보를 확인해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-002", "해당 사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-003", "이미 존재하는 사용자입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-004", "이미 존재하는 닉네임입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
