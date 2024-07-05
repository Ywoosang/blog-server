package com.ywoosang.tech.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostErrorCode {

    // 게시물 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "해당 게시물을 찾을 수 없습니다."),
    INVALID_POST_DATA(HttpStatus.BAD_REQUEST, "POST-002", "게시물 정보가 잘못되었습니다. 다시 확인해주세요."),
    DUPLICATE_POST(HttpStatus.CONFLICT, "POST-003", "이미 존재하는 게시물입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
