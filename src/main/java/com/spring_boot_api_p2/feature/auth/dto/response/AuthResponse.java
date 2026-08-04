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

    private String Token;
    private String Type;
    private UserResponse user;
    public static AuthResponse of (String token, UserResponse user){
        return  AuthResponse.builder()
                .Token(token)
                .Type("Bearer")
                .user(user)
                .build();
    }
}