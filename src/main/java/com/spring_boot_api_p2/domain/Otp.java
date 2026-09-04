package com.spring_boot_api_p2.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stores the latest OTP state for a user's forgot-password flow.
 *
 * <p><strong>One row per user</strong> ({@code user_id} is unique). Each new OTP send
 * overwrites the previous code rather than creating history rows — keeps the table small
 * and simplifies "verify latest code" logic.
 *
 * <p><strong>Security:</strong> the 6-digit code is never stored in plain text.
 * Only {@link #otpEncrypted} (AES-256-GCM ciphertext) is persisted. At verify time,
 * the service decrypts and compares in memory, then marks {@link #verified} = true
 * to prevent replay.
 *
 * <p>Note: {@code Otp} does not extend {@link BaseEntity} — it has its own id and
 * only partial auditing ({@code createdAt}/{@code updatedAt}), no soft delete.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp")
@EntityListeners(AuditingEntityListener.class)
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Foreign key to {@code users.id} — one OTP record per user.
     * Unique constraint enforces overwrite semantics on resend.
     */
    @Column(nullable = false, unique = true)
    private Integer userId;

    /**
     * AES-256-GCM encrypted OTP code (Base64 blob: salt || iv || ciphertext).
     * See {@link com.jpa.encryption.impl.EncryptionServiceImpl}.
     */
    @Column(length = 512)
    private String otpEncrypted;

    /** Absolute expiry — set to {@code now + otp.ttl-minutes} when the code is issued. */
    private Instant expiresAt;

    /** Running count of sends — compared against {@code otp.max-send-count} for lockout. */
    private Integer sentCount;

    /** Timestamp of the most recent send — drives cooldown between resends. */
    private Instant lastSentAt;

    /**
     * Single-use flag. Set to {@code true} after successful verification so the same
     * code cannot be reused even before {@link #expiresAt}.
     */
    private Boolean verified;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
