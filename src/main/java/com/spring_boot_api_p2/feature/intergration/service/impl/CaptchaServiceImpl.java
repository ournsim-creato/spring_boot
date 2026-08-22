package com.spring_boot_api_p2.feature.intergration.captcha.dto.service.impl;

import com.spring_boot_api_p2.feature.intergration.captcha.component.RandomCodeGenerator;
import com.spring_boot_api_p2.feature.intergration.captcha.dto.response.CaptchaResponse;
import com.spring_boot_api_p2.feature.intergration.captcha.dto.service.CaptchaService;
import com.spring_boot_api_p2.feature.intergration.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";

    private final RedisService redisService;
    private final RandomCodeGenerator randomCodeGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CaptchaResponse generate() {
        String captchaId = UUID.randomUUID().toString();
        String generateCode = randomCodeGenerator.generate(6);

        // Encrypt / Hash លេខកូដជាមុន
        String encryptedCode = passwordEncoder.encode(generateCode);

        // រក្សាទុកតម្លៃដែល Encrypted រួចក្នុង Redis (សុពលភាព 5 នាទី)
        redisService.save(
                CAPTCHA_KEY_PREFIX + captchaId,
                encryptedCode,
                Duration.ofMinutes(5)
        );
       
        return CaptchaResponse.builder()
                .captchaId(captchaId)
                .imageBase64(generateCode)
                .build();
    }

    @Override
    public void validate(String captchaId, String captchaData) {
        if (captchaId == null || captchaData == null || captchaData.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Captcha ID or Captcha code is required");
        }

        String redisKey = CAPTCHA_KEY_PREFIX + captchaId;
        Object cachedValue = redisService.get(redisKey);

        if (cachedValue == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Captcha has expired or is invalid");
        }

        String encryptedCodeInRedis = cachedValue.toString();
        boolean isMatch = passwordEncoder.matches(captchaData.trim(), encryptedCodeInRedis);

        // លុប Key ចេញពី Redis ភ្លាមៗ (One-time use)
        redisService.delete(redisKey);

        if (!isMatch) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Captcha code");
        }
    }
}