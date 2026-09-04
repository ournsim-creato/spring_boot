package com.spring_boot_api_p2.feature.intergration.service.impl;

import com.spring_boot_api_p2.encryption.EncryptionService;
import com.spring_boot_api_p2.feature.intergration.captcha.component.RandomCodeGenerator;
import com.spring_boot_api_p2.feature.intergration.captcha.dto.response.CaptchaResponse;
import com.spring_boot_api_p2.feature.intergration.redis.RedisService;
import com.spring_boot_api_p2.feature.intergration.service.CaptchaService;
import com.spring_boot_api_p2.property.CaptchaProperties;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private final RedisService redisService;
    private final CaptchaProperties properties;

    private final RandomCodeGenerator randomCodeGenerator;
    private final EncryptionService encryptionService;

    @Override
    public CaptchaResponse generate() {
        // Feature flag - client should skip captcha fields when false
        if (!properties.isEnabled()) {
            log.debug("captcha generate skipped - disabled");
            return CaptchaResponse.builder()
                    .enabled(false)
                    .build(); // no captchaId / imageBase64
        }

        String captchaId = UUID.randomUUID().toString();
        String generateCode = randomCodeGenerator.generate(properties.getLength());

        log.info("Captcha Code: {} | ID: {}", generateCode, captchaId);

        // ១. រក្សាទុក Encrypted Code ទៅក្នុង Redis
        redisService.save(CAPTCHA_KEY_PREFIX + captchaId,
                encryptionService.encrypt(generateCode),
                Duration.ofMinutes(properties.getTtlMinutes())
        );

        // ២. បំប្លែង generateCode ទៅជា រូបភាព Base64 ស្អាត និងពិបាកឱ្យ Bot ស្កេន
        String imageBase64 = renderCaptchaImageToBase64(generateCode);

        // ៣. បញ្ជូន Response ត្រឡប់ទៅវិញ
        return CaptchaResponse.builder()
                .captchaId(captchaId)
                .captchaData(generateCode)
                .imageBase64(imageBase64)
                .enabled(true)
                .build();
    }

    @Override
    public void validate(String captchaId, String captchaData) {
        if (!properties.isEnabled()) {
            return;
        }

        // Load ciphertext, DELETE KEY immediately
        String storeCode = getStoredCaptchaCode(captchaId);

        if (!storeCode.equalsIgnoreCase(captchaData.trim())) {
            throw new ValidationException("Incorrect captcha. Please try again");
        }
    }

    private String getStoredCaptchaCode(String captchaId) {
        String key = CAPTCHA_KEY_PREFIX + captchaId;
        Optional<String> stored = redisService.get(key);

        redisService.remove(key);

        if (stored.isEmpty()) {
            throw new ValidationException("Captcha expired. Please try again");
        }

        return encryptionService.decrypt(stored.get());
    }

    // Advanced Helper Method សម្រាប់បង្កើតរូបភាព Captcha ជា Base64 String
    public String renderCaptchaImageToBase64(String text) {
        int width = 180;
        int height = 55;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 1. Smooth rendering (ធ្វើឱ្យរូបភាពច្បាស់រលោង)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 2. Background (ពណ៌ប្រផេះស្រាល)
        g2d.setColor(new Color(242, 244, 247));
        g2d.fillRect(0, 0, width, height);

        Random random = new Random();

        // 3. គូរចំណុចរំខាន (Noise Dots) ការពារ Bot
        for (int i = 0; i < 150; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.fillRect(x, y, 2, 2);
        }

        // 4. គូរខ្សែរំខាន (Interfering Lines)
        for (int i = 0; i < 6; i++) {
            g2d.setColor(new Color(100 + random.nextInt(100), 100 + random.nextInt(100), 100 + random.nextInt(100)));
            int x1 = random.nextInt(width / 2);
            int y1 = random.nextInt(height);
            int x2 = width / 2 + random.nextInt(width / 2);
            int y2 = random.nextInt(height);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 5. គូរអក្សរ Captcha ដោយបង្វិលអក្សរនីមួយៗ (Rotated Letters)
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        int xOffset = 20;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            // បង្វិលអក្សរចន្លោះពី -25 ទៅ +25 ដឺក្រេ
            double angle = (random.nextDouble() - 0.5) * 0.8;

            g2d.translate(xOffset, 38);
            g2d.rotate(angle);

            // ដាក់ពណ៌អក្សរក្រមៅៗ (Dark Colors)
            g2d.setColor(new Color(20 + random.nextInt(80), 20 + random.nextInt(80), 20 + random.nextInt(80)));
            g2d.drawString(String.valueOf(ch), 0, 0);

            // Reset ការបង្វិលមកវិញ សម្រាប់អក្សរបន្ទាប់
            g2d.rotate(-angle);
            g2d.translate(-xOffset, -38);

            xOffset += 35; // រំកិលទៅអក្សរបន្ទាប់
        }

        g2d.dispose();

        // 6. បំប្លែងទៅជា Base64 String
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            log.error("Error generating captcha image Base64", e);
            throw new RuntimeException("Failed to generate captcha image", e);
        }
    }
}
