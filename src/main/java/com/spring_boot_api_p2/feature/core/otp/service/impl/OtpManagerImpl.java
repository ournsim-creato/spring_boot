package com.spring_boot_api_p2.feature.core.otp.service.impl;

import com.spring_boot_api_p2.property.OtpProperties;
import com.spring_boot_api_p2.domain.Otp;
import com.spring_boot_api_p2.encryption.EncryptionService;
import com.spring_boot_api_p2.feature.core.otp.repository.OtpRepository;
import com.spring_boot_api_p2.feature.core.otp.service.OtpManager;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpManagerImpl implements OtpManager {

    private final OtpRepository otpRepository;
    private final EncryptionService encryptionService;
    private final OtpProperties properties;

    @Override
    public Otp create(Integer userId, String code) {
        Instant now = Instant.now();

        Otp otp = otpRepository.findByUserId(userId)
                .orElseGet(() -> Otp.builder()
                        .userId(userId)
                        .sentCount(0)
                        .verified(false)
                        .build());

        if (otp.getLastSentAt() != null) {
            Instant nextAllowed = otp.getLastSentAt().plus(properties.getCooldownMinutes(), ChronoUnit.MINUTES);
            if (now.isBefore(nextAllowed)) {
                long seconds = Duration.between(now, nextAllowed).getSeconds();
                throw new ValidationException("Please wait " + seconds + "s before requesting another OTP");
            }
        }

        if (otp.getSentCount() >= properties.getMaxSendCount()) {
            throw new ValidationException("OTP limit reached");
        }

        otp.setOtpEncrypted(encryptionService.encrypt(code));
        otp.setExpiresAt(now.plus(properties.getTtlMinutes(), ChronoUnit.MINUTES));
        otp.setSentCount(otp.getSentCount() + 1);
        otp.setLastSentAt(now);
        otp.setVerified(false);

        log.info("OTP issued — userId={}, send {}/{}", userId, otp.getSentCount(), properties.getMaxSendCount());

        return otpRepository.save(otp);
    }

    @Override
    public void verify(Otp otp, String code) {
        if (Boolean.TRUE.equals(otp.getVerified())) {
            throw new ValidationException("OTP already used");
        }

        if (otp.getExpiresAt() == null || Instant.now().isAfter(otp.getExpiresAt())) {
            throw new ValidationException("OTP expired");
        }

        String decrypted = encryptionService.decrypt(otp.getOtpEncrypted());

        if (!decrypted.equals(code)) {
            throw new ValidationException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        log.info("OTP verified — userId={}", otp.getUserId());
    }

    /**
     * Read-only check for the optional /verify step — same guards as {@link #verify},
     * but never mutates or persists the row, so the code stays usable for the
     * real reset-password call afterward.
     */
    @Override
    public boolean check(Otp otp, String code) {
        if (Boolean.TRUE.equals(otp.getVerified())) {
            return false;
        }
        if (otp.getExpiresAt() == null || Instant.now().isAfter(otp.getExpiresAt())) {
            return false;
        }
        String decrypted = encryptionService.decrypt(otp.getOtpEncrypted());
        return decrypted.equals(code);
    }
}
