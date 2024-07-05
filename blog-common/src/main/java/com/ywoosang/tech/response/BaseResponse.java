package com.ywoosang.tech.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse {
    Boolean isSuccess;
    String message;
}
