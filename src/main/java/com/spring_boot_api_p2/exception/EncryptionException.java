package com.spring_boot_api_p2.exception;
/**
 * AES encrypt/decrypt failure.
 * Mapped to HTTP 500 by {@link GlobalExceptionHandler} (do not leak crypto details).
 */
public class EncryptionException extends RuntimeException {

    public EncryptionException(String message, Throwable cause) {
        super(message, cause); // cause kept for server logs
    }
}
