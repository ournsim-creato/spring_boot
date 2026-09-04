package com.spring_boot_api_p2.feature.core.otp.service;


import com.spring_boot_api_p2.domain.Otp;

/**
 * Persistence + crypto helper for OTP records (one row per user).
 * Implemented by {@link com.jpa.feature.otp.service.impl.OtpManagerImpl}.
 *
 * <p>Plaintext codes never touch the database — only AES-GCM ciphertext is stored.
 */
public interface OtpManager {

    /**
     * Create or refresh this user's OTP row with a new plaintext code.
     * Enforces cooldown between resends and a max send count per user.
     *
     * @param userId owner of the OTP row
     * @param code   freshly generated plaintext code (encrypted before save)
     * @return the persisted {@link Otp} entity
     */
    Otp create(Integer userId, String code);

    /**
     * Check the code against the stored ciphertext, then mark it consumed.
     * Rejects already-used, expired, or mismatched codes.
     *
     * @param otp  the loaded OTP row for this user
     * @param code plaintext code the user submitted
     */
    void verify(Otp otp, String code);

    boolean check(Otp otp, String code);
}
