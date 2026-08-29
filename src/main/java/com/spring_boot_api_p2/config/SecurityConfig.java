package com.spring_boot_api_p2.config;

import com.spring_boot_api_p2.security.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring_boot_api_p2.security.JwtAuthFilter;
import com.spring_boot_api_p2.security.RestAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Allow browser frontends on listed origins to call the API
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // API uses JSON Bearer tokens — disable CSRF form protection
                .csrf(AbstractHttpConfigurer::disable)
                // No browser popup login
                .httpBasic(AbstractHttpConfigurer::disable)
                // No Spring form login page
                .formLogin(AbstractHttpConfigurer::disable)
                // Do not create an HttpSession per request
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Write JSON BaseError instead of redirect/HTML on 401
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Register, login, change-password — no token required
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/captcha/**").permitAll()
                        .requestMatchers("/api/profile-image/**").permitAll()

                        // ** បន្ថែមបន្ទាត់នេះចូលទីនេះ ដើម្បីបើកឱ្យ API របស់ User ទាំងអស់អាចចូលបានដោយមិនបាច់ Token **
                        .requestMatchers("/api/users/**").permitAll()

                        .requestMatchers("/error").permitAll()
                        // OpenAPI / Swagger UI help

                        // Everything else needs a valid JWT in SecurityContext
                        .anyRequest().authenticated()
                )
                // JwtAuthFilter runs before Spring's default username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

// Authentication
// Authorization
// Create User
// Update User

// Redis

// captcha