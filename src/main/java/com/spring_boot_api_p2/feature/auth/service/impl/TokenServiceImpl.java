package com.spring_boot_api_p2.feature.auth.service.impl;

import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.auth.service.TokenService;
import com.spring_boot_api_p2.feature.core.role.user.response.UserResponse;
import com.spring_boot_api_p2.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final JwtService jwtService;
    @Override
    public AuthResponse issue(UserResponse user) {
        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.of(token,user);
    }
}
