package com.ywoosang.tech.domain.auth.service;

import com.ywoosang.tech.code.AuthErrorCode;
import com.ywoosang.tech.domain.auth.dto.TokenResponse;
import com.ywoosang.tech.domain.auth.jwt.JwtService;
import com.ywoosang.tech.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisAuthService redisService;
    private JwtService jwtService;

    public TokenResponse reissueToken(String refreshToken) {
        log.info("refreshToken 을 이용해 accessToken 재발급: {}", refreshToken);

        // refresh 토큰 만료여부 검사
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.TOKEN_EXPIRED);
        }
        Long memberId = jwtService.getMemberIdFromRefreshToken(refreshToken);
        String redisKey = redisService.generateRefreshTokenKey(memberId);
        String redisRefreshToken = redisService.getValue(redisKey);
        // 유효한 jwt 토큰이라면, 사용자의 refreshToken 과 대조한다.
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            log.warn("사용자의 refreshToken 이 아님: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        String newAccessToken = jwtService.createAccessToken(memberId);
        return TokenResponse.builder().accessToken(newAccessToken).build();
    }
}
