package com.ywoosang.tech.domain.auth.jwt;

import com.ywoosang.tech.domain.auth.oauth2.dto.CustomOAuth2User;
import com.ywoosang.tech.domain.auth.oauth2.dto.OAuth2UserDTO;
import com.ywoosang.tech.domains.member.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.resolveToken(request);
        OAuth2UserDTO oauth2UserDTO;
        // 로그인한 사용자
        if(accessToken != null && !jwtService.isAccessTokenExpired(accessToken)) {
            Member member = jwtService.getMemberFromAccessToken(accessToken);
            oauth2UserDTO = OAuth2UserDTO.from(member);

        }
        // 로그인하지 않은 사용자
        else {
            // authenticated() 를 거쳐야 하니까 ContextHolder 에 GUEST 로 집어넣음
            oauth2UserDTO = OAuth2UserDTO.from(null);
        }

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
