package com.ywoosang.tech.domain.auth.oauth2;

import com.ywoosang.tech.domain.auth.oauth2.dto.CustomOAuth2User;
import com.ywoosang.tech.domain.auth.jwt.JwtService;
import com.ywoosang.tech.domain.auth.oauth2.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Value("${frontend.auth-redirect-url}")
    private String authRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = customUserDetails.getMemberId();
        // jwt 생성
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);

        // 쿠키에 추가
        response.addCookie(cookieUtil.createAccessTokenCookie(accessToken));
        response.addCookie(cookieUtil.createRefreshTokenCookie(refreshToken));
        // 프론트엔드로 리다이렉트
        response.sendRedirect(authRedirectUrl);
    }
}
