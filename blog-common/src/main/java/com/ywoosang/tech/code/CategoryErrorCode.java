package com.ywoosang.tech.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CategoryErrorCode {

    // 카테고리 관련 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATE-001", "해당 카테고리를 찾을 수 없습니다."),
    INVALID_CATEGORY_DATA(HttpStatus.BAD_REQUEST, "CATE-002", "카테고리 정보가 잘못되었습니다. 다시 확인해주세요."),
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "CATE-003", "이미 존재하는 카테고리입니다."),
    CATEGORY_NAME_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "CATE-004", "카테고리 이름 길이가 허용 범위를 초과했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
