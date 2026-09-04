package com.spring_boot_api_p2.feature.auth.service;

import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
// ត្រូវប្រាកដថា import UserResponse ពី dto.response នេះ
import com.spring_boot_api_p2.feature.core.user.dto.response.UserResponse;

public interface TokenService {
    AuthResponse issue(com.spring_boot_api_p2.feature.core.role.user.response.UserResponse user);

    AuthResponse issue(UserResponse user);
}
