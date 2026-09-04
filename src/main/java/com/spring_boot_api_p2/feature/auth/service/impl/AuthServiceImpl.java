package com.spring_boot_api_p2.feature.auth.service.impl;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.auth.UserValidator;
import com.spring_boot_api_p2.feature.auth.dto.request.LoginRequest;
import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.auth.service.AuthService;
import com.spring_boot_api_p2.feature.auth.service.TokenService;
import com.spring_boot_api_p2.mapper.UserMapper; //  ប្តូរមកកាន់ Import ត្រឹមត្រូវនេះ
import com.spring_boot_api_p2.feature.intergration.service.CaptchaService;
import com.spring_boot_api_p2.property.CaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserValidator userValidator;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final CaptchaService captchaService;
    private final CaptchaProperties properties;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {

//        System.out.println(new BCryptPasswordEncoder().encode("Admin@123"));
        // =========================
        // 1. Validate CAPTCHA
        // =========================
        if (properties.isEnabled()) {

            if (request.getCaptcha() == null) {
                throw new IllegalArgumentException("CAPTCHA is required");
            }

            captchaService.validate(
                    request.getCaptcha().getCaptchaId(),
                    request.getCaptcha().getCaptchaData()
            );
        }

        // =========================
        // 2. Validate username/password
        // =========================
        User user = userValidator.validateLoginCredentials(
                request.getUsername(),
                request.getPassword()
        );

        // =========================
        // 3. Generate JWT
        // =========================
        return tokenService.issue(
                userMapper.toResponse(user)
        );
    }
}
