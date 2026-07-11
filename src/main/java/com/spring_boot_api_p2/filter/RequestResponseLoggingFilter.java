package com.spring_boot_api_p2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startAt = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startAt;String query = request.getQueryString() != null ? "?" + request.getQueryString() : "";

            // Request by client
            log.info("--> {} {}{} | client={}",
                    request.getMethod(), request.getRequestURI(), query, request.getRemoteAddr());

            // Response to client
            log.info("<-- {} {} | {} ms", response.getStatus(), request.getRequestURI(), durationMs);
        }
    }
}