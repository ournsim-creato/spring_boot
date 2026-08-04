package com.spring_boot_api_p2.feature.auth.service;

import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.core.role.user.response.UserResponse;

public interface TokenService {
    AuthResponse issue(UserResponse user);

}
