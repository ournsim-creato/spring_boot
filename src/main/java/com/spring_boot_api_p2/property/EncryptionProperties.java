package com.spring_boot_api_p2.property;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Binds {@code encryption.*} keys from application.yaml / env vars.
 * Used by EncryptionServiceImpl at startup ({@code @PostConstruct}).
 */
@Configuration
@ConfigurationProperties(prefix = "encryption")
@Data
public class EncryptionProperties {

    /**
     * Base64-encoded 32-byte AES-256 key.
     * Generate with: {@code openssl rand -base64 32}
     */
    private String secretKey;
}