package com.spring_boot_api_p2.security;
import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {


        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );


        return org.springframework.security.core.userdetails.User
                .builder()

                .username(user.getUsername())

                .password(user.getPassword())


                .authorities(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role.getName()
                                        )
                                )
                                .toList()
                )


                .accountExpired(
                        !user.getAccountNonExpired()
                )

                .accountLocked(
                        !user.getAccountNonLocked()
                )

                .credentialsExpired(
                        !user.getCredentialsNonExpired()
                )

                .disabled(
                        !user.getEnabled()
                )

                .build();

    }

}
