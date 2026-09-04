package com.spring_boot_api_p2.feature.auth.controller;

import com.spring_boot_api_p2.base.BaseApi;
import com.spring_boot_api_p2.feature.auth.dto.request.LoginRequest;
import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseApi<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(
                BaseApi.<AuthResponse>builder()
                        .status(true)
                        .code(200)
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(authResponse)
                        .build()
        );
    }
}
