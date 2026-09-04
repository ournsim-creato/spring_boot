package com.spring_boot_api_p2.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // 1) Pull token from Authorization header (may be null on public routes)
        System.out.println("Request");
        System.out.println(request);
        String token = resolveToken(request);
        System.out.println("token");
        System.out.println(token);



        // 2) No token or invalid signature/expiry — pass through without authentication
        //    (permitAll routes still work; protected routes fail later with 401)
        if (token == null || token.isBlank() || !jwtService.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        // 3) Valid token — read the subject (username) from claims
        String username = jwtService.getUsernameFromToken(token);

        // 4) Only set authentication if the context is still empty (avoid overwriting)
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // No authorities/roles in this slim app — authentication only
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());
            // Attach IP, session id, etc. for audit-friendly details
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5) Continue down the filter chain — controller can now read the authenticated user
        chain.doFilter(request, response);
    }
    // Stateless
    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}
