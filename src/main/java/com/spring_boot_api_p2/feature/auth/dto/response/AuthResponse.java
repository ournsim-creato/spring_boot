package com.spring_boot_api_p2.feature.auth.dto.response;

import com.spring_boot_api_p2.feature.core.role.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // ប្តូរមកអក្សរតូចវិញ ដើម្បីឱ្យ Jackson serialize ត្រូវ standard JSON
    private String token;
    private String type;
    private UserResponse user;

    public static AuthResponse of(String token, UserResponse user) {
        return AuthResponse.builder()
                .token(token) // កែជា token (អក្សរតូច)
                .type("Bearer") // កែជា type (អក្សរតូច)
                .user(user)
                .build();
    }
}