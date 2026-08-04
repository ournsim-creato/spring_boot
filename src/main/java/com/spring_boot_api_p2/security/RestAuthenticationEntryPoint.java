package com.spring_boot_api_p2.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

/**
 * 401 handler for unauthenticated requests (missing / invalid / expired token).
 *
 * <p>Spring Security invokes this when a protected route is hit without a valid
 * {@link org.springframework.security.core.Authentication} in the context — e.g. no
 * {@code Authorization} header, or {@link JwtAuthFilter} left the context empty.
 *
 * <p>Writes the same JSON {@link com.jpa.base.BaseError} shape as the rest of the API
 * via {@link SecurityErrorWriter}.
 */
@Component // wired in SecurityConfig.exceptionHandling
@RequiredArgsConstructor // constructor injection for all final fields
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper; // serialize BaseError to JSON

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        SecurityErrorWriter.write(objectMapper, response, HttpStatus.UNAUTHORIZED,
                "Authentication required. Please sign in again.", request.getRequestURI());
    }
}