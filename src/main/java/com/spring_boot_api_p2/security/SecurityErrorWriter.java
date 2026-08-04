package com.spring_boot_api_p2.security;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.spring_boot_api_p2.base.BaseError;

import java.io.IOException;
import java.util.UUID;

/**
 * Writes a JSON {@link BaseError} for security failures (401 / 403).
 *
 * <p>Package-private utility — keeps {@link RestAuthenticationEntryPoint} (and future
 * access-denied handlers) free of boilerplate response wiring. Each error gets a short
 * random {@code trackingId} so clients can quote it in support tickets.
 */
final class SecurityErrorWriter {

    private SecurityErrorWriter() {} // static helper — no instances

    /**
     * Set HTTP status, {@code Content-Type: application/json}, and write a {@link BaseError} body.
     *
     * @param instance request URI — helps clients see which path failed
     */
    static void write(ObjectMapper mapper, HttpServletResponse response, HttpStatus status,
                      String detail, String instance) throws IOException {
        // 16-char hex id — enough for log correlation without being a full UUID
        String trackingId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(
                BaseError.of(status.value(), status.getReasonPhrase(), detail)));
    }
}