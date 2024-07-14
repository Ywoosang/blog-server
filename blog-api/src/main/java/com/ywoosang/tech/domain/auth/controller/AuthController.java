package com.ywoosang.tech.domain.auth.controller;

import com.ywoosang.tech.domain.auth.dto.TokenResponse;
import com.ywoosang.tech.domain.auth.service.AuthService;
import com.ywoosang.tech.response.ResponseFactory;
import com.ywoosang.tech.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token/reissue")
    public ResponseEntity<SuccessResponse<TokenResponse>> reissueToken(@RequestParam("token") String token) {
        TokenResponse tokenResponse = authService.reissueToken(token);
        return ResponseFactory.success(tokenResponse);
    }








}
