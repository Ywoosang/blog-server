package com.ywoosang.tech.domain.auth.jwt;

import com.ywoosang.tech.code.AuthErrorCode;
import com.ywoosang.tech.domain.auth.service.RedisService;
import com.ywoosang.tech.domains.member.entity.Member;
import com.ywoosang.tech.domains.member.repository.MemberRepository;
import com.ywoosang.tech.enums.MemberRole;
import com.ywoosang.tech.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final RedisService redisService;
    MemberRepository memberRepository;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expires-in}")
    private int accessTokenExpiresIn;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expires-in}")
    private int refreshTokenExpiresIn;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void initialize() {
        System.out.println("accessTokenSecret = " + accessTokenSecret);
        System.out.println("refreshTokenSecret = " + refreshTokenSecret);

        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String createAccessToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiresIn))
                .signWith(accessTokenSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        String refreshToken = Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiresIn))
                .signWith(refreshTokenSecretKey, Jwts.SIG.HS256)
                .compact();

        String key = "refresh:" + email;
        redisService.setValue(key, refreshToken, refreshTokenExpiresIn);
        return refreshToken;
    }

    public boolean isAccessTokenExpired(String accessToken) throws CustomException {
        return isTokenExpired(accessToken, accessTokenSecretKey);
    }

    public boolean isRefreshTokenExpired(String refreshToken) throws CustomException {
        return isTokenExpired(refreshToken, refreshTokenSecretKey);
    }

    // 토큰 만료여부 검사
    private boolean isTokenExpired(String token, SecretKey secretKey) {
        // setSigningKey 가 Deprecated 되면서 verifyWith 를 사용
        // 공식문서 https://javadoc.io/doc/io.jsonwebtoken/jjwt-api/0.12.3/io/jsonwebtoken/JwtParserBuilder.html
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof MalformedJwtException) {
                log.warn("올바르지 않은 형식의 JWT 토큰: {}", e.getMessage());
            } else if (e instanceof SignatureException) {
                log.warn("JWT 서명이 일치하지 않음: {}", e.getMessage());
            } else if (e instanceof UnsupportedJwtException) {
                log.warn("토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
            } else {
                log.warn("JWT 만료기간 검사 처리 중 오류 발생: {}", e.getMessage());
            }
            throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    public Member getMemberFromAccessToken(String token) {
        String email = getEmailFromAccessToken(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    // 토큰의 만료여부 검사 후 만료되지 않았다면 호출할 것
    public String getEmailFromAccessToken(String token) {
        return Jwts.parser().verifyWith(accessTokenSecretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email",String.class);
    }

    public String getEmailFromRefreshToken(String token) {
        return Jwts.parser().verifyWith(refreshTokenSecretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email",String.class);
    }
}
