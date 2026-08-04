package com.spring_boot_api_p2.feature.auth.service.impl;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.auth.dto.request.LoginRequest;
import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.auth.service.AuthService;
import com.spring_boot_api_p2.feature.auth.service.TokenService;
import com.spring_boot_api_p2.feature.auth.validator.UserValidator;
import com.spring_boot_api_p2.feature.core.role.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserValidator userValidator;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    @Override
    public AuthResponse login(LoginRequest request) {
        // Load user + Bcrypt-compare password
        User user = userValidator.validateLoginCredentials(request.getUsername(), request.getPassword());

        // generate token

        return tokenService.issue(userMapper.toResponse(user));
    }
}