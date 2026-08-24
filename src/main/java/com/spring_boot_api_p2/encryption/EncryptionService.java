package com.spring_boot_api_p2.encryption;

public interface EncryptionService {
    String encrypt(String plaintext);
    String decrypt(String cipertext);
}
