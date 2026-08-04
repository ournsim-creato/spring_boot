package com.spring_boot_api_p2.feature.auth.service;

import com.spring_boot_api_p2.feature.auth.dto.request.LoginRequest;
import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
