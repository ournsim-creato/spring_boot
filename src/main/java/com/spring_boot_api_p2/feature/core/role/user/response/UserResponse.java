package com.spring_boot_api_p2.feature.core.role.user.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


    private Long id;


    private String username;


    private String nickName;


    private Boolean enabled;


    // User roles
    private Set<String> roles;

    public void setProfile(String profileUrl) {
    }
}
