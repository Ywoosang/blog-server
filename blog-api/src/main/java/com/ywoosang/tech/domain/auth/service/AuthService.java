package com.ywoosang.tech.domain.auth.service;

import com.ywoosang.tech.code.AuthErrorCode;
import com.ywoosang.tech.code.ErrorCode;
import com.ywoosang.tech.domain.auth.dto.TokenResponse;
import com.ywoosang.tech.domain.auth.jwt.JwtService;
import com.ywoosang.tech.domains.member.entity.Member;
import com.ywoosang.tech.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisService redisService;
    private JwtService jwtService;

    public TokenResponse reissueToken(String refreshToken) {
        log.info("refreshToken 재발급: {}", refreshToken);
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.TOKEN_EXPIRED);
        }
        String email = jwtService.getEmailFromRefreshToken(refreshToken);
        String redisKey = "refresh:" + email;
        String redisRefreshToken = redisService.getValue(redisKey);
        // 유효한 jwt 토큰이라면, 사용자의 refreshToken 과 대조해
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            log.warn("사용자의 refreshToken 이 아님: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_DENIED);
        }
        String newAccessToken = jwtService.createAccessToken(email);
        return TokenResponse.builder().accessToken(newAccessToken).build();
    }
}
